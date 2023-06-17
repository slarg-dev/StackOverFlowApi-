package stack.overflow.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import stack.overflow.model.entity.Account;
import stack.overflow.model.entity.Permission;
import stack.overflow.model.enumeration.PermissionName;
import stack.overflow.service.entity.AccountService;
import stack.overflow.service.entity.PermissionService;

import java.util.HashMap;
import java.util.Map;

@Profile("local")
@RequiredArgsConstructor
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

    private final AccountService accountService;
    private final PermissionService permissionService;

    private final Map<PermissionName, Permission> permissionMap = new HashMap<>();
    private final Map<String, Account> accountMap = new HashMap<>();

    private final int adminCount = 3;
    private final int moderatorCount = 5;
    private final int userCount = 12;

    @Override
    public void run(ApplicationArguments args) {
        addPermissions();
        addAdmins();
        addModerators();
        addUsers();
    }

    private void addPermissions() {
        permissionMap.put(PermissionName.ADMIN, permissionService.create(new Permission(PermissionName.ADMIN)));
        permissionMap.put(PermissionName.MODERATOR, permissionService.create(new Permission(PermissionName.MODERATOR)));
        permissionMap.put(PermissionName.USER, permissionService.create(new Permission(PermissionName.USER)));
    }

    private void addAdmins() {
        for (int i = 1; i <= adminCount; i++) {
            Account admin = new Account();
            String username = "admin" + i;
            admin.setUsername(username);
            admin.setPassword("admin");
            admin.addPermission(permissionMap.get(PermissionName.ADMIN));
            admin.setIsAccountEnabled(Boolean.TRUE);
            accountMap.put(username, accountService.create(admin));
        }
    }

    private void addModerators() {
        for (int i = 1; i <= moderatorCount; i++) {
            Account moderator = new Account();
            String username = "moderator" + i;
            moderator.setUsername(username);
            moderator.setPassword("moderator");
            moderator.addPermission(permissionMap.get(PermissionName.MODERATOR));
            moderator.setIsAccountEnabled(Boolean.TRUE);
            accountMap.put(username, accountService.create(moderator));
        }
    }

    private void addUsers() {
        for (int i = 1; i <= userCount; i++) {
            Account user = new Account();
            String username = "user" + i;
            user.setUsername(username);
            user.setPassword("user");
            user.addPermission(permissionMap.get(PermissionName.USER));
            user.setIsAccountEnabled(Boolean.TRUE);
            accountMap.put(username, accountService.create(user));
        }
    }
}
