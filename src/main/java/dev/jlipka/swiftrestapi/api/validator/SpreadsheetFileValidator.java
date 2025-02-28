package dev.jlipka.swiftrestapi.api.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

@Component
public class SpreadsheetFileValidator implements Validator {

    private static final int maxSizeInMb = 5;

    @Override
    public boolean supports(Class<?> clazz) {
        return MultipartFile.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MultipartFile file = (MultipartFile) target;

        isSpreadSheetFile(file, errors);
        isFileTooBig(file, errors);
    }

    private void isSpreadSheetFile(MultipartFile file, Errors errors) {
        Pattern pattern = Pattern.compile("\\.xlsx?$", Pattern.CASE_INSENSITIVE);
        String filename = requireNonNull(file.getOriginalFilename());

        Matcher matcher = pattern.matcher(filename);
        if (!matcher.find()) {
            errors.reject("file.invalid.format", "File must be in XLS or XLSX format");
        }
    }
    private void isFileTooBig(MultipartFile file, Errors errors) {
        if (convertBytesToMegaBytes(file.getSize()) > maxSizeInMb) {
            errors.reject("file.too.large",
                    String.format("File is too large (Max: %d MB)", maxSizeInMb));
        }
    }

    private long convertBytesToMegaBytes(long bytes) {
        return bytes / (1024 * 1024);
    }
}