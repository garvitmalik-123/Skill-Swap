import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api/axios'
import { errorText } from '../api/errorText'

const DIFFICULTIES = ['BEGINNER', 'INTERMEDIATE', 'ADVANCED']
const inputClass = 'w-full border rounded-lg px-3 py-2'

// turns "one item per line" text into a list of items
function toList(text) {
    return text.split('\n').map((t) => t.trim()).filter(Boolean)
}

function Field({ label, children }) {
    return (
        <label className="block space-y-1">
            <span className="text-sm text-gray-600">{label}</span>
            {children}
        </label>
    )
}

function CreateCourse() {
    const navigate = useNavigate()
    const [categories, setCategories] = useState([])
    const [allSkills, setAllSkills] = useState([])

    const [title, setTitle] = useState('')
    const [description, setDescription] = useState('')
    const [category, setCategory] = useState('')
    const [skills, setSkills] = useState([])
    const [difficulty, setDifficulty] = useState('BEGINNER')
    const [language, setLanguage] = useState('English')
    const [durationMinutes, setDurationMinutes] = useState('')
    const [objectives, setObjectives] = useState('')
    const [prerequisites, setPrerequisites] = useState('')
    const [thumbnailUrl, setThumbnailUrl] = useState('')
    const [type, setType] = useState('FREE')
    const [price, setPrice] = useState('')
    const [skillPointCost, setSkillPointCost] = useState('')

    const [saving, setSaving] = useState(false)
    const [error, setError] = useState('')

    useEffect(() => {
        api.get('/categories').then((res) => setCategories(res.data)).catch(() => {})
        api.get('/skills').then((res) => setAllSkills(res.data)).catch(() => {})
    }, [])

    // click a skill chip: add it if missing, remove it if already chosen
    function toggleSkill(name) {
        setSkills((current) =>
            current.includes(name) ? current.filter((s) => s !== name) : [...current, name]
        )
    }

    async function handleSubmit(e) {
        e.preventDefault()
        setError('')
        if (!category) {
            setError('Please choose a category.')
            return
        }
        setSaving(true)
        try {
            const body = {
                title,
                description,
                category,
                skills,
                difficulty,
                language,
                type,
                learningObjectives: toList(objectives),
                prerequisites: toList(prerequisites),
            }
            if (durationMinutes) body.durationMinutes = Number(durationMinutes)
            if (thumbnailUrl.trim()) body.thumbnailUrl = thumbnailUrl.trim()
            if (type === 'PAID') body.price = Number(price)
            if (type === 'SKILLPOINT') body.skillPointCost = Math.round(Number(skillPointCost))

            const res = await api.post('/courses', body)
            navigate(`/courses/${res.data.data.id}/manage`)
        } catch (err) {
            setError(errorText(err))
        } finally {
            setSaving(false)
        }
    }

    return (
        <div className="min-h-[calc(100vh-64px)] bg-indigo-50 p-6">
            <form onSubmit={handleSubmit} className="max-w-2xl mx-auto bg-white rounded-xl shadow p-6 space-y-4">
                <h1 className="text-2xl font-bold text-indigo-600">Create a course</h1>
                {error && <p className="text-red-600 text-sm">{error}</p>}

                <Field label="Title">
                    <input className={inputClass} value={title} onChange={(e) => setTitle(e.target.value)} required />
                </Field>

                <Field label="Description">
                    <textarea className={inputClass} rows={4} value={description} onChange={(e) => setDescription(e.target.value)} required />
                </Field>

                <div className="grid md:grid-cols-2 gap-4">
                    <Field label="Category">
                        <select className={inputClass} value={category} onChange={(e) => setCategory(e.target.value)}>
                            <option value="">Choose...</option>
                            {categories.map((c) => <option key={c.id} value={c.name}>{c.name}</option>)}
                        </select>
                    </Field>
                    <Field label="Difficulty">
                        <select className={inputClass} value={difficulty} onChange={(e) => setDifficulty(e.target.value)}>
                            {DIFFICULTIES.map((d) => <option key={d} value={d}>{d}</option>)}
                        </select>
                    </Field>
                    <Field label="Language">
                        <input className={inputClass} value={language} onChange={(e) => setLanguage(e.target.value)} />
                    </Field>
                    <Field label="Total duration (minutes)">
                        <input className={inputClass} type="number" min="1" value={durationMinutes} onChange={(e) => setDurationMinutes(e.target.value)} />
                    </Field>
                </div>

                <div className="space-y-1">
                    <span className="text-sm text-gray-600">Skills this course covers (click to select)</span>
                    <div className="flex flex-wrap gap-2">
                        {allSkills.map((s) => (
                            <button
                                type="button"
                                key={s.id}
                                onClick={() => toggleSkill(s.name)}
                                className={`px-3 py-1 rounded-full text-sm border ${
                                    skills.includes(s.name)
                                        ? 'bg-indigo-600 text-white border-indigo-600'
                                        : 'bg-white text-gray-600'
                                }`}
                            >
                                {s.name}
                            </button>
                        ))}
                    </div>
                </div>

                <Field label="What will learners learn? (one per line)">
                    <textarea className={inputClass} rows={3} value={objectives} onChange={(e) => setObjectives(e.target.value)} />
                </Field>

                <Field label="Prerequisites (one per line)">
                    <textarea className={inputClass} rows={2} value={prerequisites} onChange={(e) => setPrerequisites(e.target.value)} />
                </Field>

                <Field label="Thumbnail image link (optional)">
                    <input className={inputClass} value={thumbnailUrl} onChange={(e) => setThumbnailUrl(e.target.value)} />
                </Field>

                <div className="grid md:grid-cols-2 gap-4">
                    <Field label="Course type">
                        <select className={inputClass} value={type} onChange={(e) => setType(e.target.value)}>
                            <option value="FREE">Free</option>
                            <option value="SKILLPOINT">SkillPoints</option>
                            <option value="PAID">Paid</option>
                        </select>
                    </Field>
                    {type === 'PAID' && (
                        <Field label="Price">
                            <input className={inputClass} type="number" min="1" value={price} onChange={(e) => setPrice(e.target.value)} required />
                        </Field>
                    )}
                    {type === 'SKILLPOINT' && (
                        <Field label="SkillPoint cost">
                            <input className={inputClass} type="number" min="1" value={skillPointCost} onChange={(e) => setSkillPointCost(e.target.value)} required />
                        </Field>
                    )}
                </div>

                <button disabled={saving} className="px-5 py-2 rounded-lg bg-indigo-600 text-white disabled:opacity-50">
                    {saving ? 'Creating...' : 'Create course'}
                </button>
            </form>
        </div>
    )
}

export default CreateCourse