import axios from "axios";

const authHeaders = (token) => ({
    Authorization: `Bearer ${token}`,
});

export const saveInvoice = (baseURL, payload, token) =>
    axios.post(`${baseURL}/invoices`, payload, {
        headers: authHeaders(token),
    });

export const getAllInvoices = (baseURL, token) =>
    axios.get(`${baseURL}/invoices`, {
        headers: authHeaders(token),
    });

export const deleteInvoice = (baseURL, id, token) =>
    axios.delete(`${baseURL}/invoices/${id}`, {
        headers: authHeaders(token),
    });

export const sendInvoice = (baseURL, file, customerEmail, token) => {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("email", customerEmail);

    return axios.post(`${baseURL}/invoices/sendinvoice`, formData, {
        headers: authHeaders(token),
    });
};