package in.sirajshaik.billingsoftware.service.impl;

import in.sirajshaik.billingsoftware.entity.UserEntity;
import in.sirajshaik.billingsoftware.io.UserRequest;
import in.sirajshaik.billingsoftware.io.UserResponse;
import in.sirajshaik.billingsoftware.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public UserResponse createUser(UserRequest request) {

        UserEntity newUser = convertToEntity(request);
        newUser =  userRepository.save(newUser);
        return convertToResponse(newUser);
    }

    private UserResponse convertToResponse(UserEntity newUser) {

        return   UserResponse.builder()
                .userId(newUser.getUserId())
                .name(newUser.getName())
                .email(newUser.getEmail())
                .role(newUser.getRole())
                .createdAt(newUser.getCreatedAt())
                .updatedAt(newUser.getUpdatedAt())
                .build();

    }

    private UserEntity convertToEntity(UserRequest request) {
        return   UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole().toUpperCase())
                .build();
    }

    @Override
    public String getUserRole(String email) {
      UserEntity  existingUser  = userRepository.findByEmail(email).
                orElseThrow(()-> new UsernameNotFoundException("User not found for email : "+email));
        return existingUser.getRole();
    }

    @Override
    public List<UserResponse> readUsers() {
        return    userRepository.findAll()
                   .stream()
                   .map(user -> convertToResponse(user))
                   .collect(Collectors.toList());

    }

    @Override
    public void deleteUser(String id) {
    UserEntity  existingUser = userRepository.findByUserId(id)
                        .orElseThrow(()-> new UsernameNotFoundException("User Not Found "));
                userRepository.delete(existingUser);
    }
}
