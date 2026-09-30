param(
    [ValidatePattern('^https?://')]
    [string] $BaseUrl = 'http://localhost:8080',

    [ValidatePattern('^https?://')]
    [string] $ServerUrl = $BaseUrl
)

$ErrorActionPreference = 'Stop'

$normalizedBaseUrl = $BaseUrl.TrimEnd('/')
$apiDocsUrl = "$normalizedBaseUrl/v3/api-docs"

Write-Host "Reading OpenAPI from $apiDocsUrl"
$document = Invoke-RestMethod -Uri $apiDocsUrl -Method Get
# Keep documentation-only descriptions when refreshing the backend contract.
$detailsPath = Join-Path $PSScriptRoot 'endpoint-details.json'
$details = Get-Content -LiteralPath $detailsPath -Raw | ConvertFrom-Json -AsHashtable
$document.info | Add-Member -NotePropertyName description -NotePropertyValue $details.info.description -Force
foreach ($pathProperty in $details.paths.GetEnumerator()) {
    $targetPath = $document.paths.PSObject.Properties[$pathProperty.Key]
    if ($null -eq $targetPath) {
        Write-Warning "Documented path missing from backend contract: $($pathProperty.Key)"
        continue
    }
    foreach ($methodProperty in $pathProperty.Value.GetEnumerator()) {
        $operation = $targetPath.Value.PSObject.Properties[$methodProperty.Key]
        if ($null -eq $operation) {
            Write-Warning "Documented method missing: $($methodProperty.Key) $($pathProperty.Key)"
            continue
        }
        foreach ($field in $methodProperty.Value.GetEnumerator()) {
            $operation.Value | Add-Member -NotePropertyName $field.Key -NotePropertyValue $field.Value -Force
        }
    }
}
foreach ($pathProperty in $document.paths.PSObject.Properties) {
    foreach ($method in @('get', 'post', 'put', 'patch', 'delete', 'options', 'head', 'trace')) {
        $operation = $pathProperty.Value.$method
        if ($null -eq $operation) { continue }
        foreach ($parameter in $operation.parameters) {
            $description = $details.parameters[$parameter.name]
            if ($null -ne $description) {
                $parameter | Add-Member -NotePropertyName description -NotePropertyValue $description -Force
            }
        }
    }
}
$document.servers = @([ordered]@{
    url = $ServerUrl.TrimEnd('/')
    description = 'Generated server URL'
})

$utf8WithoutBom = New-Object System.Text.UTF8Encoding($false)
$apiPath = Join-Path $PSScriptRoot 'api.json'
$apiJson = $document | ConvertTo-Json -Depth 100
[System.IO.File]::WriteAllText($apiPath, $apiJson, $utf8WithoutBom)

# Embed the same contract so double-clicking index.html does not require fetch().
$viewerPath = Join-Path $PSScriptRoot 'index.html'
$viewer = [System.IO.File]::ReadAllText($viewerPath, [System.Text.Encoding]::UTF8)
$viewer = [regex]::Replace(
    $viewer,
    '(?s)\s*<!-- BEGIN QUIZ OPENAPI -->.*?<!-- END QUIZ OPENAPI -->\s*',
    ''
)
if (-not $viewer.Contains('</head>')) {
    throw 'Cannot embed the API contract: index.html has no closing head tag.'
}
$embeddedJson = ($document | ConvertTo-Json -Depth 100 -Compress).
    Replace('<', '\u003c').Replace('>', '\u003e').Replace('&', '\u0026')
$embeddedBlock = "`n  <!-- BEGIN QUIZ OPENAPI -->`n  <script>window.__QUIZ_EMBEDDED_OPENAPI__ = $embeddedJson;</script>`n  <!-- END QUIZ OPENAPI -->`n"
$viewer = $viewer.Replace('</head>', "$embeddedBlock</head>")
[System.IO.File]::WriteAllText($viewerPath, $viewer, $utf8WithoutBom)

$httpMethods = @('get', 'post', 'put', 'patch', 'delete', 'options', 'head', 'trace')
$endpointData = foreach ($pathProperty in $document.paths.PSObject.Properties) {
    foreach ($method in $httpMethods) {
        $operation = $pathProperty.Value.$method
        if ($null -eq $operation) {
            continue
        }

        [ordered]@{
            name = if ($operation.summary) { $operation.summary } else { $operation.operationId }
            operationId = $operation.operationId
            method = $method
            path = $pathProperty.Name
            status = 'released'
        }
    }
}

$endpointCatalog = [ordered]@{
    success = $true
    resource = 'endpoint'
    operation = 'list'
    source = 'docs/api/api.json'
    data = @($endpointData)
}
$catalogPath = Join-Path $PSScriptRoot '..\..\endpoints.json'
[System.IO.File]::WriteAllText(
    $catalogPath,
    ($endpointCatalog | ConvertTo-Json -Depth 10),
    $utf8WithoutBom
)

Write-Host "Generated $apiPath"
Write-Host "Updated $viewerPath with the embedded API contract"
Write-Host "Generated $catalogPath with $($endpointData.Count) operations"
