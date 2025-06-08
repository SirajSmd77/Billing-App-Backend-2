package in.sirajshaik.billingsoftware.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AuthResponse {

    private String email;

    private String token;

    private String role;
}
