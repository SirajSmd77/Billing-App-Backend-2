package in.sirajshaik.billingsoftware.controller;

import in.sirajshaik.billingsoftware.io.AuthRequest;
import in.sirajshaik.billingsoftware.io.AuthResponse;
import in.sirajshaik.billingsoftware.service.impl.AppUserDetailsService;
import in.sirajshaik.billingsoftware.service.impl.UserService;
import in.sirajshaik.billingsoftware.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private  final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final AppUserDetailsService appUserDetailsService;

    private final UserService userService;

    private final JwtUtil jwtUtil;


    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) throws Exception {

        authenticate(authRequest.getEmail(),authRequest.getPassword());
        UserDetails userDetails = appUserDetailsService.loadUserByUsername(authRequest.getEmail());
        final String jwtToken = jwtUtil.generateToken(userDetails);
        String role = userService.getUserRole(authRequest.getEmail());
        return new AuthResponse(authRequest.getEmail(),jwtToken,role);
    }

    private void authenticate(String email, String password) throws Exception {
    try{
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
    }catch (DisabledException e){
        throw new Exception("User Disabled");
    }catch (BadCredentialsException e){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"email or password is incorrect");
      }
    }

    @PostMapping("/encode")
    public String encodePassword(@RequestBody Map<String,String> request){
        return  passwordEncoder.encode(request.get("password"));

    }
}
