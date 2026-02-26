package in.kanimozhi.invoicegeneratorapi.service;

import in.kanimozhi.invoicegeneratorapi.entity.Invoice;
import in.kanimozhi.invoicegeneratorapi.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public List<Invoice> fetchInvoices(String clerkId) {
        return invoiceRepository.findByClerkId(clerkId);
    }

    // ✅ CORRECT ORDER: clerkId FIRST, invoiceId SECOND
    public void removeInvoice(String clerkId, String invoiceId) {
        Invoice existingInvoice = invoiceRepository
                .findByClerkIdAndId(clerkId, invoiceId)
                .orElseThrow(() ->
                        new RuntimeException("Invoice not found or unauthorized"));

        invoiceRepository.delete(existingInvoice);
    }
}