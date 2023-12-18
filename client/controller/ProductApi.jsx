export const productApi = {
    listAll: async () => {
        const res = await fetch("http://localhost:9080/api/products");
        return res.json()
    },
    listBasedOnProductId: async (id) => {
        const res = await fetch(`http://localhost:9080/api/products/${id}`)

        return await res.json();
    }
}