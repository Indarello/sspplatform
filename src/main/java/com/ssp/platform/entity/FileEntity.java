package com.ssp.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
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

    @OneToOne
    @JoinColumn(name="purchase_file")
    @JsonIgnore
    private Purchase purchase;

    @NotNull
    private String name;

    @NotNull
    private String mimeType;

    @NotNull
    private long size;

    @NotNull
    private String hash;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "file")
    private List<Purchase> purchases;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "file")
    private List<SupplyEntity> supplies;

    public void setHash() throws NoSuchAlgorithmException {
        String modifiedName = name + mimeType + size;
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(modifiedName.getBytes(StandardCharsets.UTF_8));
        hash = new BigInteger(1, messageDigest.digest()).toString(16);
    }

}
