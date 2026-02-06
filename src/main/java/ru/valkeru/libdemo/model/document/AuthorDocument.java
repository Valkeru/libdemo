package ru.valkeru.libdemo.model.document;

import com.querydsl.core.annotations.QueryEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.UUID;

@Getter
@Setter
@Document(indexName = "#{@elasticsearchIndexPrefix}_author", createIndex = false)
@Setting(
        shards = 2
)
@QueryEntity
public class AuthorDocument {

    @Id
    private UUID id;

    @Field(type = FieldType.Text, fielddata = true)
    private String firstName;

    @Field(type = FieldType.Text, fielddata = true)
    private String middleName;

    @Field(type = FieldType.Text, fielddata = true)
    private String lastName;
}
