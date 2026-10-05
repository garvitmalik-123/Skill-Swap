import { useEffect, useState } from 'react'
import api from '../api/axios'

const LEVELS = ['BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT']

// Turns a backend error into a readable sentence
function errorText(err) {
    const data = err.response?.data
    if (data?.errors) return Object.values(data.errors).join(', ')
    return data?.message || 'Something went wrong. Is the backend running?'
}

function SkillList({ title, emoji, items, onRemove }) {
    return (
        <div className="bg-white rounded-xl shadow p-5">
            <h3 className="font-bold text-indigo-600 mb-3">{emoji} {title}</h3>
            {items.length === 0 ? (
                <p className="text-gray-400 text-sm">Nothing here yet.</p>
            ) : (
                <ul className="flex flex-wrap gap-2">
                    {items.map((s) => (
                        <li key={s.id} className="flex items-center gap-2 bg-indigo-50 text-indigo-700 rounded-full px-3 py-1 text-sm">
                            <span>{s.skillName}</span>
                            <span className="text-xs text-indigo-400">{s.level}</span>
                            <button onClick={() => onRemove(s.skillId)} className="text-indigo-400 hover:text-red-600" title="Remove">
                                ✕
                            </button>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    )
}

function Profile() {
    const [profile, setProfile] = useState(null)
    const [skills, setSkills] = useState([])
    const [categories, setCategories] = useState([])
    const [loading, setLoading] = useState(true)
    const [loadError, setLoadError] = useState('')

    // edit-profile form
    const [bio, setBio] = useState('')
    const [userLocation, setUserLocation] = useState('')
    const [experienceLevel, setExperienceLevel] = useState('')
    const [saving, setSaving] = useState(false)
    const [profileMsg, setProfileMsg] = useState('')
    const [profileErr, setProfileErr] = useState('')

    // add-skill form
    const [skillId, setSkillId] = useState('')
    const [relationType, setRelationType] = useState('CAN_TEACH')
    const [level, setLevel] = useState('BEGINNER')
    const [adding, setAdding] = useState(false)
    const [skillErr, setSkillErr] = useState('')

    // runs once when the page opens: fetch everything we need
    useEffect(() => {
        async function loadAll() {
            try {
                const [profileRes, skillsRes, catRes] = await Promise.all([
                    api.get('/users/me/skills'),
                    api.get('/skills'),
                    api.get('/categories'),
                ])
                const p = profileRes.data
                setProfile(p)
                setBio(p.bio ?? '')
                setUserLocation(p.location ?? '')
                setExperienceLevel(p.experienceLevel ?? '')
                setSkills(skillsRes.data)
                setCategories(catRes.data)
            } catch (err) {
                setLoadError(errorText(err))
            } finally {
                setLoading(false)
            }
        }
        loadAll()
    }, [])

    async function refreshProfile() {
        const res = await api.get('/users/me/skills')
        setProfile(res.data)
    }

    async function saveProfile(e) {
        e.preventDefault()
        setProfileMsg('')
        setProfileErr('')
        setSaving(true)
        try {
            const body = { bio, location: userLocation }
            if (experienceLevel) body.experienceLevel = experienceLevel
            const res = await api.put('/users/me', body)
            setProfile(res.data)
            setProfileMsg('Profile saved ✅')
        } catch (err) {
            setProfileErr(errorText(err))
        } finally {
            setSaving(false)
        }
    }

    async function addSkill(e) {
        e.preventDefault()
        if (!skillId) {
            setSkillErr('Please choose a skill first.')
            return
        }
        setSkillErr('')
        setAdding(true)
        try {
            await api.post('/users/me/skills', { skillId, relationType, level })
            await refreshProfile()
            setSkillId('')
        } catch (err) {
            setSkillErr(errorText(err))
        } finally {
            setAdding(false)
        }
    }

    async function removeSkill(id) {
        setSkillErr('')
        try {
            await api.delete(`/users/me/skills/${id}`)
            await refreshProfile()
        } catch (err) {
            setSkillErr(errorText(err))
        }
    }

    if (loading) return <p className="p-8 text-gray-600">Loading your profile...</p>
    if (loadError) return <p className="p-8 text-red-600">{loadError}</p>

    const uncategorized = skills.filter((s) => !categories.some((c) => c.id === s.categoryId))

    return (
        <div className="min-h-[calc(100vh-64px)] bg-indigo-50 p-6">
            <div className="max-w-4xl mx-auto space-y-6">

                {/* Header card */}
                <div className="bg-white rounded-xl shadow p-6 flex items-center gap-4">
                    <div className="w-16 h-16 rounded-full bg-indigo-600 text-white flex items-center justify-center text-2xl font-bold">
                        {profile.name?.charAt(0).toUpperCase()}
                    </div>
                    <div>
                        <h1 className="text-2xl font-bold text-gray-800">{profile.name}</h1>
                        <p className="text-gray-500">{profile.email}</p>
                        {profile.experienceLevel && (
                            <p className="text-sm text-indigo-600">Level: {profile.experienceLevel}</p>
                        )}
                    </div>
                </div>

                {/* Edit profile */}
                <form onSubmit={saveProfile} className="bg-white rounded-xl shadow p-6 space-y-4">
                    <h2 className="text-lg font-bold text-indigo-600">About me</h2>

                    {profileMsg && <p className="text-green-600 text-sm">{profileMsg}</p>}
                    {profileErr && <p className="text-red-600 text-sm">{profileErr}</p>}

                    <textarea
                        className="w-full border rounded-lg px-3 py-2"
                        rows={3}
                        maxLength={500}
                        placeholder="Write a short bio (max 500 characters)"
                        value={bio}
                        onChange={(e) => setBio(e.target.value)}
                    />
                    <input
                        className="w-full border rounded-lg px-3 py-2"
                        maxLength={100}
                        placeholder="Location (for example: Gurugram, India)"
                        value={userLocation}
                        onChange={(e) => setUserLocation(e.target.value)}
                    />
                    <select
                        className="w-full border rounded-lg px-3 py-2"
                        value={experienceLevel}
                        onChange={(e) => setExperienceLevel(e.target.value)}
                    >
                        <option value="">Experience level (not set)</option>
                        {LEVELS.map((l) => <option key={l} value={l}>{l}</option>)}
                    </select>

                    <button disabled={saving} className="px-5 py-2 rounded-lg bg-indigo-600 text-white disabled:opacity-50">
                        {saving ? 'Saving...' : 'Save profile'}
                    </button>
                </form>

                {/* My skills */}
                <div className="grid md:grid-cols-2 gap-4">
                    <SkillList title="I can teach" emoji="🎓" items={profile.teachingSkills ?? []} onRemove={removeSkill} />
                    <SkillList title="I want to learn" emoji="📚" items={profile.learningSkills ?? []} onRemove={removeSkill} />
                </div>

                {/* Add a skill */}
                <form onSubmit={addSkill} className="bg-white rounded-xl shadow p-6 space-y-4">
                    <h2 className="text-lg font-bold text-indigo-600">Add a skill</h2>

                    {skillErr && <p className="text-red-600 text-sm">{skillErr}</p>}
                    {skills.length === 0 && (
                        <p className="text-amber-600 text-sm">
                            The skill catalog is empty. Add the DataSeeder to the backend and restart it.
                        </p>
                    )}

                    <div className="grid md:grid-cols-3 gap-3">
                        <select
                            className="border rounded-lg px-3 py-2"
                            value={skillId}
                            onChange={(e) => setSkillId(e.target.value)}
                        >
                            <option value="">Choose a skill...</option>
                            {categories.map((cat) => (
                                <optgroup key={cat.id} label={cat.name}>
                                    {skills.filter((s) => s.categoryId === cat.id).map((s) => (
                                        <option key={s.id} value={s.id}>{s.name}</option>
                                    ))}
                                </optgroup>
                            ))}
                            {uncategorized.map((s) => (
                                <option key={s.id} value={s.id}>{s.name}</option>
                            ))}
                        </select>

                        <select
                            className="border rounded-lg px-3 py-2"
                            value={relationType}
                            onChange={(e) => setRelationType(e.target.value)}
                        >
                            <option value="CAN_TEACH">I can teach this</option>
                            <option value="WANTS_TO_LEARN">I want to learn this</option>
                        </select>

                        <select
                            className="border rounded-lg px-3 py-2"
                            value={level}
                            onChange={(e) => setLevel(e.target.value)}
                        >
                            {LEVELS.map((l) => <option key={l} value={l}>{l}</option>)}
                        </select>
                    </div>

                    <button disabled={adding} className="px-5 py-2 rounded-lg bg-indigo-600 text-white disabled:opacity-50">
                        {adding ? 'Adding...' : 'Add skill'}
                    </button>
                </form>

            </div>
        </div>
    )
}

export default Profile