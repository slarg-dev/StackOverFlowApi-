package stack.overflow.service.entity.impl;

import org.springframework.stereotype.Service;
import stack.overflow.model.entity.Permission;
import stack.overflow.model.repository.entity.PermissionRepository;
import stack.overflow.service.crud.impl.CrudServiceImpl;
import stack.overflow.service.entity.PermissionService;

@Service
public class PermissionServiceImpl extends CrudServiceImpl<Permission, Long> implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        super(permissionRepository);
        this.permissionRepository = permissionRepository;
    }
}
