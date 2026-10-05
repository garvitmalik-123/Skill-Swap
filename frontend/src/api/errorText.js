// Turns a backend error into a sentence a human can read
export function errorText(err) {
    const data = err.response?.data
    if (data?.errors) return Object.values(data.errors).join(', ')
    return data?.message || 'Something went wrong. Is the backend running?'
}