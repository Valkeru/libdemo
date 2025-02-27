package ru.valkeru.libdemo.helper;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.service.BookService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookHelper implements ApplicationContextAware {

    static BookService bookService;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        initDependencies(applicationContext);
    }

    public static boolean authorHasBooks(Long authorId) {
        return bookService.countBooksByAuthorId(authorId) > 0;
    }

    public static boolean cycleContainsBooks(Long cycleId) {
        return bookService.countBooksByCycleId(cycleId) > 0;
    }

    private static void initDependencies(ApplicationContext context) {
        bookService = context.getBean(BookService.class);
    }
}
