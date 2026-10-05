import { Routes, Route } from 'react-router-dom'
import Home from './pages/Home'
import Login from './pages/Login'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'
import Profile from './pages/Profile'
import Courses from './pages/Courses'
import CourseDetails from './pages/CourseDetails'
import CreateCourse from './pages/CreateCourse'
import ManageCourse from './pages/ManageCourse'
import MyCourses from './pages/MyCourses'
import Navbar from './components/Navbar'
import ProtectedRoute from './components/ProtectedRoute'

function App() {
    return (
        <>
            <Navbar />
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/courses" element={<Courses />} />
                <Route path="/courses/:id" element={<CourseDetails />} />

                <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
                <Route path="/profile" element={<ProtectedRoute><Profile /></ProtectedRoute>} />
                <Route path="/my-courses" element={<ProtectedRoute><MyCourses /></ProtectedRoute>} />
                <Route path="/courses/new" element={<ProtectedRoute><CreateCourse /></ProtectedRoute>} />
                <Route path="/courses/:id/manage" element={<ProtectedRoute><ManageCourse /></ProtectedRoute>} />
            </Routes>
        </>
    )
}

export default App