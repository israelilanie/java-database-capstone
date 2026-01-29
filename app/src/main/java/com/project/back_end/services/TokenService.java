package com.project.back_end.services;

import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Component
public class TokenService {

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey signingKey;

    private final long EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000L; // 7 days

    // ---------------------------------------------------
    // Constructor Injection
    // ---------------------------------------------------
    public TokenService(AdminRepository adminRepository,
                        DoctorRepository doctorRepository,
                        PatientRepository patientRepository) {
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    // ---------------------------------------------------
    // Initialize Signing Key
    // ---------------------------------------------------
    @PostConstruct
    public void init() {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // ---------------------------------------------------
    // Generate JWT Token
    // ---------------------------------------------------
    public String generateToken(String identifier) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(identifier)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateDoctorToken(Long doctorId) {
        return generateToken(String.valueOf(doctorId));
    }

    // ---------------------------------------------------
    // Extract Identifier (Subject) from Token
    // ---------------------------------------------------
    public String extractIdentifier(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public String extractEmail(String token) {
        return extractIdentifier(token);
    }

    public Long extractPatientId(String token) {
        String email = extractIdentifier(token);
        if (email == null) {
            return null;
        }
        return Optional.ofNullable(patientRepository.findByEmail(email))
                .map(patient -> patient.getId())
                .orElse(null);
    }

    public Long extractDoctorId(String token) {
        String identifier = extractIdentifier(token);
        if (identifier == null) {
            return null;
        }
        try {
            return Long.parseLong(identifier);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    // ---------------------------------------------------
    // Validate Token for a Given User Type
    // ---------------------------------------------------
    public boolean validateToken(String token, String userType) {
        try {
            String identifier = extractIdentifier(token);

            switch (userType.toLowerCase()) {
                case "admin":
                    return adminRepository.findByUsername(identifier) != null;
                case "doctor":
                    Long doctorId = extractDoctorId(token);
                    return doctorId != null && doctorRepository.existsById(doctorId);
                case "patient":
                    return patientRepository.findByEmail(identifier) != null;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    // ---------------------------------------------------
    // Get Signing Key
    // ---------------------------------------------------
    public SecretKey getSigningKey() {
        return signingKey;
    }
}
