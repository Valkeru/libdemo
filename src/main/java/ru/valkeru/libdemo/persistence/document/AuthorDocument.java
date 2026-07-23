package ru.valkeru.libdemo.persistence.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.Setting;
import ru.valkeru.libdemo.constants.CommonConstants;

import java.util.UUID;

@Getter
@Setter
@Document(indexName = "#{@appElasticsearchProperties.authorIndexName}", createIndex = false)
@Setting(
        shards = 2
)
public class AuthorDocument {

    public static final String ES_FIRST_NAME_FIELD_NAME = "firstName";
    public static final String ES_MIDDLE_NAME_FIELD_NAME = "middleName";
    public static final String ES_LAST_NAME_FIELD_NAME = "lastName";

    @Id
    private UUID id;

    @MultiField(
        mainField = @Field(name = ES_FIRST_NAME_FIELD_NAME, type = FieldType.Text),
        otherFields = {
            @InnerField(type = FieldType.Keyword, suffix = CommonConstants.ES_KEYWORD_FIELD_SUFFIX),
            @InnerField(type = FieldType.Search_As_You_Type, suffix = CommonConstants.ES_PREFIX_FIELD_SUFFIX)
        }
    )
    private String firstName;

    @MultiField(
        mainField = @Field(name = ES_MIDDLE_NAME_FIELD_NAME, type = FieldType.Text),
        otherFields = {
            @InnerField(type = FieldType.Keyword, suffix = CommonConstants.ES_KEYWORD_FIELD_SUFFIX),
            @InnerField(type = FieldType.Search_As_You_Type, suffix = CommonConstants.ES_PREFIX_FIELD_SUFFIX)
        }
    )
    private String middleName;

    @MultiField(
        mainField = @Field(name = ES_LAST_NAME_FIELD_NAME, type = FieldType.Text),
        otherFields = {
            @InnerField(type = FieldType.Keyword, suffix = CommonConstants.ES_KEYWORD_FIELD_SUFFIX),
            @InnerField(type = FieldType.Search_As_You_Type, suffix = CommonConstants.ES_PREFIX_FIELD_SUFFIX)
        }
    )
    private String lastName;

    public static String prefixField(String field) {
        return "%s.%s".formatted(field, CommonConstants.ES_PREFIX_FIELD_SUFFIX);
    }
}
