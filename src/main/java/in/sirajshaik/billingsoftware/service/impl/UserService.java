package in.sirajshaik.billingsoftware.service.impl;

import in.sirajshaik.billingsoftware.io.UserRequest;
import in.sirajshaik.billingsoftware.io.UserResponse;

import java.util.List;

public interface UserService {


    UserResponse createUser(UserRequest request);

    String getUserRole(String email);

    List<UserResponse> readUsers();

    void deleteUser(String id);
}
