export const orderApi = {
    listAll: async () => {
        const res = await fetch("http://localhost:9080/api/orders");
        return res.json()
    },
    insert: async ({userId, productId, productName, orderDate, status, productAmount, totalAmount, orderItems}) => {
        const res = await fetch("http://localhost:9080/api/orders/save", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                userId,
                productId,
                productName,
                orderDate,
                status,
                productAmount,
                totalAmount,
                orderItems,
            }),
        });

        return res.ok;
    },
    update: async ({ id, userId, productId, productName, orderDate, status, productAmount, totalAmount, orderItems }) => {
        const res = await fetch("http://localhost:9080/api/orders", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                id,
                userId,
                productId,
                productName,
                orderDate,
                status,
                productAmount,
                totalAmount,
                orderItems,
            }),
        });

        return res.ok;
    },
    delete: async (id) => {
        const res = await fetch("http://localhost:9080/api/orders/delete", {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({id,}),
        });

        return res.ok;
    },
    listBasedOnUser: async (userId) => {
        const res = await fetch(`http://localhost:9080/api/orders/${userId}`)

        return await res.json();
    }
}