package Jay.BoardP.controller.dto;

import Jay.BoardP.domain.Member;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@EqualsAndHashCode(of = "username")
public class User implements UserDetails {

    private final Member member;

    private String username;

    public User(Member member) {
        this.member = member;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(member.getRole().getValue()));
        return roles;
    }


    @Override
    public String getPassword() {
        return member.getPassword();
    }

    public String getNickname() {
        return member.getNickname();
    }

    public Member getMember() {
        return member;
    }

    public Enum<Role> getRole() {
        return member.getRole();
    }

    @Override
    public String getUsername() {
        return member.getUserId();
    }

    public Long getId() {
        return member.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
