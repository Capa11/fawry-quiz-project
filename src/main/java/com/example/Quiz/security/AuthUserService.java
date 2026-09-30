package com.example.Quiz.security;

import com.example.Quiz.model.User;
import com.example.Quiz.model.UserAuthProvider;
import com.example.Quiz.model.UserEmailPassword;
import com.example.Quiz.model.enums.Providers;
import com.example.Quiz.repository.UserAuthProviderRepository;
import com.example.Quiz.repository.UserEmailPasswordRepository;
import com.example.Quiz.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class AuthUserService extends DefaultOAuth2UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserEmailPasswordRepository userEmailPasswordRepository;
    @Autowired
    UserAuthProviderRepository authProviderRepository;
    @Autowired
    private UserAuthProviderRepository userAuthProviderRepository;

    //userdetails service
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException{
        AuthUser authUser;
        if(emailOrUsername.contains("@")){
            UserEmailPassword userEmailPassword = userEmailPasswordRepository.findByEmail(emailOrUsername).orElseThrow(()->new UsernameNotFoundException("User not found with email: " + emailOrUsername + ". Please register."));
            authUser = new AuthUser(userEmailPassword.getUser(),userEmailPassword);
        }
        else {

            User user = userRepository.findByUsername(emailOrUsername).orElseThrow(()->new UsernameNotFoundException("User not found with username: " + emailOrUsername + ". Please register."));
            UserEmailPassword userEmailPassword = userEmailPasswordRepository.findByUser(user).orElseThrow();
            authUser = new AuthUser(userEmailPassword.getUser(),userEmailPassword);
        }
        return authUser;
    }

    //oauth2 service
    String username;
    String idAttributeKey;
    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String email = oAuth2User.getAttribute("email");
        String name = attributes.get("name").toString();
        username = " ";
        idAttributeKey = "";
        if(provider.equals("github")){
            username = attributes.getOrDefault("login","").toString();
            idAttributeKey =  attributes.getOrDefault("id","").toString();
        }
        else if(provider.equals("google")){
            username = attributes.getOrDefault("email","").toString();
            idAttributeKey =  attributes.getOrDefault("sub","").toString();

        }
        else{
            throw new OAuth2AuthenticationException(new OAuth2Error("This provider is not supported"));
        }

        AtomicReference<AuthUser> authUser = new AtomicReference<>();
        userAuthProviderRepository.findByProviderAndProviderUserId(Providers.valueOf(provider), idAttributeKey).
                ifPresentOrElse(
                        userAuthProvider -> {
                            authUser.set(new AuthUser(userAuthProvider.getUser(),attributes));
                        },
                        ()->{
                            User user = new User(name,username);
                            UserAuthProvider userAuthProvider = new UserAuthProvider(user,Providers.valueOf(provider),idAttributeKey);
                            userRepository.save(user);
                            userAuthProviderRepository.save(userAuthProvider);
                            authUser.set(new AuthUser(user,attributes));

                });
        return authUser.get();
    }

}
