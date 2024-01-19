package com.milko.awsfilestorage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "events")
public class EventEntity {
    @Id
    private Integer id;
    @Column("user_id")
    private Integer userId;
    @Column("file_id")
    private Integer fileId;
    private Status status;
}
