package productivo.registration.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import productivo.registration.models.Role;
import productivo.registration.repos.RoleRepo;

@Component
@Service
public class RoleService {

    @Autowired
    private RoleRepo rrepo;

    public Role findRoleByRoleName(String name) {
        return rrepo.findByName(name);
    }

    public Role saveRole(Role r) {
        return rrepo.save(r);
    }
}