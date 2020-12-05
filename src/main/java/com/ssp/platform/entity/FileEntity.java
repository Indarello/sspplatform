package com.ssp.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.sql.Timestamp;
import java.util.*;

@Data
@Entity
@Table(name = "files")
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @JsonIgnore
    @Column(name="purchase_file")
    private UUID purchase;

    @JsonIgnore
    @Column(name = "supply_id")
    private UUID supply;

    @NotNull
    private String name;

    @NotNull
    private String mimeType;

    @NotNull
    private long size;

    @NotNull
    private String hash;

    public void setHash() throws NoSuchAlgorithmException {
        String modifiedName = name + mimeType + size + new Timestamp(System.nanoTime());
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(modifiedName.getBytes(StandardCharsets.UTF_8));
        hash = new BigInteger(1, messageDigest.digest()).toString(16);
    }

}
