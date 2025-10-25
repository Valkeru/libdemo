package ru.valkeru.libdemo.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.valkeru.libdemo.annotation.Secret;
import ru.valkeru.libdemo.model.view.AuthorView;
import ru.valkeru.libdemo.model.view.BookView;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Автор")
public class AuthorDto {

    public AuthorDto(UUID id, String firstName, String middleName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    @Secret
    @JsonView({
            AuthorView.AuthorListView.class,
            AuthorView.AuthorSingleView.class,
            BookView.BookCreateView.class,
    })
    @NotNull(groups = {
            AuthorView.AuthorUpdateView.class,
            AuthorView.AuthorListView.class,
            BookView.BookCreateView.class,
    })
    @Schema(description = "ID записи")
    private UUID id;

    @JsonView({
            AuthorView.AuthorCreateView.class,
            AuthorView.AuthorSingleView.class
    })
    @NotNull
    @NotBlank
    @Size(max = 255)
    @Schema(description = "Имя", example = "Михаил")
    private String firstName;

    @JsonView({
            AuthorView.AuthorCreateView.class,
            AuthorView.AuthorSingleView.class
    })
    @Size(max = 255)
    @Schema(description = "Отчество или второе имя (имена)", example = "Афанасьевич")
    private String middleName;

    @JsonView({
            AuthorView.AuthorCreateView.class,
            AuthorView.AuthorSingleView.class
    })
    @NotBlank
    @Size(max = 255)
    @Schema(description = "Фамилия", example = "Булгаков")
    private String lastName;

    @JsonView({
            AuthorView.AuthorListView.class,
            BookView.BookListView.class,
            BookView.BookSingleView.class
    })
    @Schema(name = "fullName", description = "Полное имя", example = "Раймонд Элиас Фэйст")
    public String getFullName() {
        return String.format("%s %s %s", firstName, middleName, lastName);
    }
}
