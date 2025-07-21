package org.openmrs.module.privilegehelper;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.openmrs.Privilege;
import org.openmrs.Role;

import java.util.stream.Collectors;

@Data
@JsonPropertyOrder({ "uuid", "roleName", "description", "inheritedRoles", "privileges" })
public class RoleExportEntry {

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("Role name")
    private String roleName;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Inherited roles")
    private String inheritedRoles;

    @JsonProperty("Privileges")
    private String privileges;

    public RoleExportEntry(Role role) {
        this.uuid = role.getUuid();
        this.roleName = role.getName();
        this.description = role.getDescription();

        this.inheritedRoles = role.getAllParentRoles() != null ?
                role.getAllParentRoles().stream().map(Role::getRole).collect(Collectors.joining(";"))
                : "";

        this.privileges = role.getPrivileges() != null ?
                role.getPrivileges().stream().map(Privilege::getPrivilege).collect(Collectors.joining(";"))
                : "";
    }
}
