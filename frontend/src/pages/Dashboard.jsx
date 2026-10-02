import { useAuth } from '../context/AuthContext'

function Dashboard() {
    const { user } = useAuth()

    return (
        <div className="min-h-[calc(100vh-64px)] bg-indigo-50 p-8">
            <div className="max-w-xl mx-auto bg-white p-6 rounded-xl shadow space-y-2">
                <h1 className="text-2xl font-bold text-indigo-600">Welcome, {user.name}! 🎉</h1>
                <p className="text-gray-600">{user.email}</p>
                <p className="text-gray-400 text-sm">
                    Your learning dashboard will live here soon.
                </p>
            </div>
        </div>
    )
}

export default Dashboard