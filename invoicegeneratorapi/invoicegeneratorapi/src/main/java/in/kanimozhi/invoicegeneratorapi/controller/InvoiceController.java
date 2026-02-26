package in.kanimozhi.invoicegeneratorapi.controller;

import in.kanimozhi.invoicegeneratorapi.entity.Invoice;
import in.kanimozhi.invoicegeneratorapi.service.EmailService;
import in.kanimozhi.invoicegeneratorapi.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final EmailService emailService;

    public InvoiceController(InvoiceService invoiceService,
                             EmailService emailService) {
        this.invoiceService = invoiceService;
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<Invoice> saveInvoice(
            @RequestBody Invoice invoice,
            Authentication authentication
    ) {
        invoice.setClerkId(authentication.getName());
        return ResponseEntity.ok(invoiceService.saveInvoice(invoice));
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> fetchInvoices(Authentication authentication) {
        return ResponseEntity.ok(
                invoiceService.fetchInvoices(authentication.getName())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeInvoice(
            @PathVariable String id,
            Authentication authentication
    ) {
        invoiceService.removeInvoice(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }

    // ✅ AUTHENTICATION ADDED
    @PostMapping("/sendinvoice")
    public ResponseEntity<?> sendInvoice(
            @RequestPart("file") MultipartFile file,
            @RequestPart("email") String customerEmail,
            Authentication authentication
    ) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized");
        }

        if (customerEmail == null || !customerEmail.contains("@")) {
            return ResponseEntity.badRequest().body("Invalid email address");
        }

        try {
            emailService.sendInvoiceEmail(customerEmail, file);
            return ResponseEntity.ok("Invoice sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send invoice");
        }
    }
}