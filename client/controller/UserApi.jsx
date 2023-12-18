export const userApi = {
    listAll: async () => {
        //using gateway localhost
        const res = await fetch("http://localhost:9080/api/users");
        return res.json()
    },
}