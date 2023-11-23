/*
 * Copyright (c) 2010 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package org.github.flytreeleft.nexus3.keycloak.plugin.internal;

import java.util.Collections;
import java.util.Set;
import java.util.List;
import javax.enterprise.inject.Typed;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.security.authz.AbstractReadOnlyAuthorizationManager;
import org.sonatype.nexus.security.authz.AuthorizationManager;
import org.sonatype.nexus.security.privilege.NoSuchPrivilegeException;
import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.security.role.NoSuchRoleException;
import org.sonatype.nexus.security.role.Role;

@Singleton
@Named("Keycloak")
@Typed(AuthorizationManager.class)
public class KeycloakAuthorizationManager extends AbstractReadOnlyAuthorizationManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private NexusKeycloakClient client;

    public KeycloakAuthorizationManager() {
        this(NexusKeycloakClientLoader.loadDefaultClient());
    }

    public KeycloakAuthorizationManager(NexusKeycloakClient client) {
        this.logger.info(getClass().getName() + " is starting...");
        this.client = client;
    }

    @Override
    public String getSource() {
        return this.client.getSource();
    }

    @Override
    public Privilege getPrivilege(String privilegeId) throws NoSuchPrivilegeException {
        throw new NoSuchPrivilegeException("Keycloak plugin doesn't support privileges");
    }

    @Override
    public Role getRole(String roleId) throws NoSuchRoleException {
        Role role = this.client.findRoleByRoleId(roleId);
        if (role == null) {
            throw new NoSuchRoleException("Failed to get role " + roleId + " from Keycloak.");
        } else {
            return role;
        }
    }

    @Override
    public Privilege getPrivilegeByName(String privilegeName) throws NoSuchPrivilegeException {
        throw new NoSuchPrivilegeException("Keycloak plugin doesn't support privileges");
    }

    @Override
    public List<Privilege> getPrivileges(Set<String> privilegeIds) {
        return Collections.emptyList();
    }

    @Override
    public Set<Privilege> listPrivileges() {
        return Collections.emptySet();
    }

    @Override
    public Set<Role> listRoles() {
        return this.client.findRoles();
    }
}
