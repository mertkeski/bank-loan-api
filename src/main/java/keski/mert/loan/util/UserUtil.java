package keski.mert.loan.util;

import keski.mert.loan.exception.NotAuthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {

    private UserUtil() {
    }

    private static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public static void checkAccess(String customerName) {
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !getCurrentUsername().equals(customerName)) {
            throw new NotAuthorizedException();
        }
    }

}
