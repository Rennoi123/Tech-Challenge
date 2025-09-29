package com.example.techchallenge.adapters.gateways;

import com.example.techchallenge.core.interfaces.ISecurityGateway;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityGatewayAdapter implements ISecurityGateway {

    @Override
    public String getAuthenticatedEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}