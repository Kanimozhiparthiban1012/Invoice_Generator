import axios from "axios";

const authHeaders = (token) => {
    if (!token) throw new Error("JWT token missing");
    return { Authorization: `Bearer ${token}` };
};

// ================= SAVE =================
export const saveInvoice = (baseURL, payload, token) =>
    axios.post(`${baseURL}/invoices`, payload, {
        headers: authHeaders(token),
    });

// ================= FETCH =================
export const getAllInvoices = (baseURL, token) =>
    axios.get(`${baseURL}/invoices`, {
        headers: authHeaders(token),
    });

// ================= DELETE =================
export const deleteInvoice = (baseURL, id, token) =>
    axios.delete(`${baseURL}/invoices/${id}`, {
        headers: authHeaders(token),
    });

// ================= SEND EMAIL (FIXED) =================
export const sendInvoice = (baseURL, formData, token) =>
    axios.post(
        `${baseURL}/invoices/sendinvoice`,
        formData,
        {
            headers: {
                ...authHeaders(token),
                "Content-Type": "multipart/form-data",
            },
        }
    );