import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import api from '../api/axios'
import { errorText } from '../api/errorText'
import { useAuth } from '../context/AuthContext'
import StatusBadge from '../components/StatusBadge'

const inputClass = 'w-full border rounded-lg px-3 py-2'

function ManageCourse() {
    const { id } = useParams()
    const { user } = useAuth()

    const [course, setCourse] = useState(null)
    const [lessons, setLessons] = useState([])
    const [loading, setLoading] = useState(true)
    const [loadError, setLoadError] = useState('')
    const [actionError, setActionError] = useState('')

    // new-lesson form
    const [title, setTitle] = useState('')
    const [content, setContent] = useState('')
    const [videoUrl, setVideoUrl] = useState('')
    const [lessonMinutes, setLessonMinutes] = useState('')
    const [adding, setAdding] = useState(false)

    useEffect(() => {
        async function load() {
            try {
                const [courseRes, lessonsRes] = await Promise.all([
                    api.get(`/courses/${id}`),
                    api.get(`/courses/${id}/lessons`),
                ])
                setCourse(courseRes.data.data)
                setLessons(lessonsRes.data)
            } catch (err) {
                setLoadError(errorText(err))
            } finally {
                setLoading(false)
            }
        }
        load()
    }, [id])

    async function refreshLessons() {
        const res = await api.get(`/courses/${id}/lessons`)
        setLessons(res.data)
    }

    // action is 'publish' or 'archive'
    async function changeStatus(action) {
        setActionError('')
        try {
            const res = await api.post(`/courses/${id}/${action}`)
            setCourse(res.data.data)
        } catch (err) {
            setActionError(errorText(err))
        }
    }

    async function addLesson(e) {
        e.preventDefault()
        setActionError('')
        setAdding(true)
        try {
            const nextOrder = lessons.length > 0 ? Math.max(...lessons.map((l) => l.order)) + 1 : 1
            const body = { title, content, order: nextOrder }
            if (videoUrl.trim()) body.videoUrl = videoUrl.trim()
            if (lessonMinutes) body.durationMinutes = Number(lessonMinutes)
            await api.post(`/courses/${id}/lessons`, body)
            setTitle('')
            setContent('')
            setVideoUrl('')
            setLessonMinutes('')
            await refreshLessons()
        } catch (err) {
            setActionError(errorText(err))
        } finally {
            setAdding(false)
        }
    }

    async function publishLesson(lessonId) {
        setActionError('')
        try {
            await api.post(`/courses/${id}/lessons/${lessonId}/publish`)
            await refreshLessons()
        } catch (err) {
            setActionError(errorText(err))
        }
    }

    async function deleteLesson(lessonId) {
        if (!window.confirm('Delete this lesson?')) return
        setActionError('')
        try {
            await api.delete(`/courses/${id}/lessons/${lessonId}`)
            await refreshLessons()
        } catch (err) {
            setActionError(errorText(err))
        }
    }

    if (loading) return <p className="p-8 text-gray-600">Loading...</p>
    if (loadError) return <p className="p-8 text-red-600">{loadError}</p>
    if (course.creatorId !== user.id) {
        return <p className="p-8 text-red-600">Only the owner of this course can manage it.</p>
    }

    const hasPublishedLesson = lessons.some((l) => l.published)

    return (
        <div className="min-h-[calc(100vh-64px)] bg-indigo-50 p-6">
            <div className="max-w-3xl mx-auto space-y-6">

                {/* Course header */}
                <div className="bg-white rounded-xl shadow p-6 space-y-3">
                    <div className="flex items-center gap-3">
                        <h1 className="text-2xl font-bold text-gray-800">{course.title}</h1>
                        <StatusBadge status={course.status} />
                    </div>

                    {actionError && <p className="text-red-600 text-sm">{actionError}</p>}

                    {course.status === 'DRAFT' && !hasPublishedLesson && (
                        <p className="text-amber-600 text-sm">
                            Tip: publish at least one lesson before publishing the course. Learners only see published lessons.
                        </p>
                    )}

                    <div className="flex flex-wrap gap-3">
                        {course.status !== 'PUBLISHED' && course.status !== 'ARCHIVED' && (
                            <button onClick={() => changeStatus('publish')} className="px-4 py-2 rounded-lg bg-indigo-600 text-white">
                                Publish course
                            </button>
                        )}
                        {course.status === 'PUBLISHED' && (
                            <button onClick={() => changeStatus('archive')} className="px-4 py-2 rounded-lg border border-indigo-600 text-indigo-600">
                                Archive course
                            </button>
                        )}
                        <Link to={`/courses/${id}`} className="px-4 py-2 rounded-lg border text-gray-600">
                            View public page
                        </Link>
                    </div>
                </div>

                {/* Lessons */}
                <div className="bg-white rounded-xl shadow p-6">
                    <h2 className="font-bold text-indigo-600 mb-3">Lessons</h2>
                    {lessons.length === 0 ? (
                        <p className="text-gray-400 text-sm">No lessons yet. Add your first one below.</p>
                    ) : (
                        <ul className="space-y-2">
                            {lessons.map((l) => (
                                <li key={l.id} className="flex items-center justify-between border rounded-lg px-3 py-2">
                                    <div>
                                        <span className="font-medium text-gray-700">{l.order}. {l.title}</span>
                                        {l.durationMinutes && <span className="ml-2 text-xs text-gray-400">{l.durationMinutes} min</span>}
                                    </div>
                                    <div className="flex items-center gap-3">
                                        {l.published ? (
                                            <span className="text-xs text-green-600">Published</span>
                                        ) : (
                                            <button onClick={() => publishLesson(l.id)} className="text-sm text-indigo-600">
                                                Publish
                                            </button>
                                        )}
                                        <button onClick={() => deleteLesson(l.id)} className="text-sm text-red-500">
                                            Delete
                                        </button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>

                {/* Add lesson */}
                <form onSubmit={addLesson} className="bg-white rounded-xl shadow p-6 space-y-3">
                    <h2 className="font-bold text-indigo-600">Add a lesson</h2>
                    <input className={inputClass} placeholder="Lesson title" value={title} onChange={(e) => setTitle(e.target.value)} required />
                    <textarea className={inputClass} rows={4} placeholder="Lesson content (text)" value={content} onChange={(e) => setContent(e.target.value)} />
                    <div className="grid md:grid-cols-2 gap-3">
                        <input className={inputClass} placeholder="Video link (optional)" value={videoUrl} onChange={(e) => setVideoUrl(e.target.value)} />
                        <input className={inputClass} type="number" min="1" placeholder="Duration in minutes (optional)" value={lessonMinutes} onChange={(e) => setLessonMinutes(e.target.value)} />
                    </div>
                    <button disabled={adding} className="px-5 py-2 rounded-lg bg-indigo-600 text-white disabled:opacity-50">
                        {adding ? 'Adding...' : 'Add lesson'}
                    </button>
                </form>

            </div>
        </div>
    )
}

export default ManageCourse