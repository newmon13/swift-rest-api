package dev.jlipka.swiftrestapi.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import static java.util.Objects.requireNonNull;

@Component
public class XlsxValidator implements Validator {
    private static final int maxSizeInMb = 5;

    @Override
    public boolean supports(Class<?> clazz) {
        return MultipartFile.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MultipartFile file = (MultipartFile) target;

        if (!isXlsxFile(file)) {
            errors.reject("file.invalid.format", "File must be in XLSX format");
        }

        if (!isFileTooBig(file)) {
            errors.reject("file.too.large",
                    String.format("File is too large (Max: %d MB)", maxSizeInMb));
        }
    }

    private boolean isXlsxFile(MultipartFile file) {
        return (requireNonNull(file.getOriginalFilename())
                .toLowerCase().endsWith(".xlsx"));
    }

    private boolean isFileTooBig(MultipartFile file) {
       return convertBytesToMegaBytes(file.getSize()) >= maxSizeInMb);
    }

    private long convertBytesToMegaBytes(long bytes) {
        return bytes / (1024 * 1024);
    }
}