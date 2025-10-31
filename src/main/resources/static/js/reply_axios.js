async function addReply(replyObj){
    const response = await axios.post(`/replies/`, replyObj)
    console.log(response)
    return response.data
}

async function getReply(rno){
    const response = await axios.get(`/replies/${rno}`)
    return response.data
}

async function modifyReply(replyObj){
    const response = await axios.put(`/replies/${replyObj.rno}`, replyObj)
    return response.data
}

async function deleteReply(rno) {
    const response = await axios.delete(`/replies/${rno}`)
    return response.data
}

async function getList({bno, page, size, goLast}){
    const response = await axios.get(`/replies/list/${bno}`, {params: {page, size}})
    if(goLast) {
        const total = response.data.total;
        const lastPage = response.data.last
        return getList({bno: bno, page: lastPage, size: size})
    }
    return response.data
}