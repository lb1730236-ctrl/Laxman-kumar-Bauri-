package com.example.data

import com.example.model.ChatMessage
import com.example.model.CompatibilityBreakdown
import com.example.model.MessageType
import com.example.model.SoulMatchThread
import com.example.model.SoulUser
import com.example.model.VibeOption
import com.example.model.VibeQuizQuestion

object SoulSampleData {

    val deepIcebreakers = listOf(
        "What is a truth about yourself you rarely share with anyone?",
        "When in your life have you felt the most deeply understood?",
        "What does an emotionally safe relationship look like to you?",
        "If you could relive one peaceful moment forever, what would it be?",
        "What kind of energy do you hope to welcome into your life right now?",
        "What song feels like it was written directly about your soul?",
        "Do you believe two people can be destined to cross paths across time?",
        "What little thing made you genuinely smile or pause in awe today?"
    )

    val sampleUsers = listOf(
        SoulUser(
            id = "user_seraphina",
            name = "Seraphina Vance",
            age = 26,
            zodiac = "Pisces ♓",
            location = "San Francisco, CA • 3 miles away",
            bio = "Astrophysics researcher by day, stargazing poet by night. Searching for a mind that asks endless questions and a heart that stays gentle.",
            soulArchetype = "Cosmic Mystic",
            compatibilityScore = 98,
            breakdown = CompatibilityBreakdown(emotional = 99, intellectual = 97, spiritual = 98, lifestyle = 92),
            loveLanguage = "Deep Conversation & Quality Time",
            interests = listOf("Cosmology", "Vinyl Records", "Midnight Walks", "Tea Rituals", "Philosophical Debates"),
            deepPromptQuestion = "What intimacy means to me...",
            deepPromptAnswer = "Sitting in complete silence without a drop of anxiety, knowing both of our souls are completely anchored.",
            avatarGradient = listOf(0xFF8B5CF6, 0xFFEC4899, 0xFFF43F5E),
            voiceIntroDurationSec = 22,
            isOnline = true
        ),
        SoulUser(
            id = "user_kiran",
            name = "Kiran Sterling",
            age = 28,
            zodiac = "Scorpio ♏",
            location = "Brooklyn, NY • Online Now",
            bio = "Ambient sound designer & landscape architect. I believe connection isn't something you find, it's something you resonate into existence.",
            soulArchetype = "Resonant Creator",
            compatibilityScore = 94,
            breakdown = CompatibilityBreakdown(emotional = 96, intellectual = 93, spiritual = 92, lifestyle = 91),
            loveLanguage = "Physical Presence & Thoughtful Gestures",
            interests = listOf("Soundscapes", "Analog Cameras", "Botanical Gardens", "Bouldering", "French Cinema"),
            deepPromptQuestion = "The conversation I want to have with my soulmate...",
            deepPromptAnswer = "The one where we unpack our childhood dreams and realize our weirdest habits are the very things that make us whole.",
            avatarGradient = listOf(0xFF6366F1, 0xFFA855F7, 0xFF06B6D4),
            voiceIntroDurationSec = 17,
            isOnline = true
        ),
        SoulUser(
            id = "user_elena",
            name = "Elena Rostova",
            age = 25,
            zodiac = "Cancer ♋",
            location = "Chicago, IL • 8 miles away",
            bio = "Classical cellist & cognitive psychologist. Looking for someone whose vulnerability feels like courage, not weakness.",
            soulArchetype = "Empathetic Harmonizer",
            compatibilityScore = 91,
            breakdown = CompatibilityBreakdown(emotional = 94, intellectual = 90, spiritual = 93, lifestyle = 88),
            loveLanguage = "Words of Affirmation & Soft Touches",
            interests = listOf("Cello", "Art Galleries", "Espresso Tasting", "Psychology", "Poetry"),
            deepPromptQuestion = "The sign that someone is my soulmate...",
            deepPromptAnswer = "When their laughter makes my nervous system immediately exhale and feel completely at peace.",
            avatarGradient = listOf(0xFFD946EF, 0xFFF472B6, 0xFFFB7185),
            voiceIntroDurationSec = 19,
            isOnline = false
        ),
        SoulUser(
            id = "user_julian",
            name = "Julian Mercer",
            age = 29,
            zodiac = "Libra ♎",
            location = "Austin, TX • 12 miles away",
            bio = "Architectural preservationist & ceramicist. Crafting intentional spaces where deep conversations and late night tea can thrive.",
            soulArchetype = "Sacred Architect",
            compatibilityScore = 88,
            breakdown = CompatibilityBreakdown(emotional = 87, intellectual = 92, spiritual = 86, lifestyle = 89),
            loveLanguage = "Acts of Service & Shared Projects",
            interests = listOf("Ceramics", "Mid-Century Design", "Campfires", "Acoustic Guitar", "Hiking"),
            deepPromptQuestion = "My philosophy on lifelong love...",
            deepPromptAnswer = "Not two people gazing only at each other, but two souls looking outward together in the exact same direction.",
            avatarGradient = listOf(0xFF3B82F6, 0xFF8B5CF6, 0xFFEC4899),
            voiceIntroDurationSec = 15,
            isOnline = true
        ),
        SoulUser(
            id = "user_amara",
            name = "Amara Solis",
            age = 27,
            zodiac = "Sagittarius ♐",
            location = "Seattle, WA • Online Now",
            bio = "Wildlife photographer & mindfulness facilitator. If you're willing to book a spontaneous cabin trip to watch the aurora borealis, say hi.",
            soulArchetype = "Luminous Nomad",
            compatibilityScore = 86,
            breakdown = CompatibilityBreakdown(emotional = 84, intellectual = 89, spiritual = 91, lifestyle = 82),
            loveLanguage = "Adventure & Unfiltered Freedom",
            interests = listOf("Nature Photography", "Meditation", "Backpacking", "Constellations", "Kayaking"),
            deepPromptQuestion = "The moment I feel most alive...",
            deepPromptAnswer = "Standing at the edge of a foggy pine forest at sunrise when the world is completely still.",
            avatarGradient = listOf(0xFFF59E0B, 0xFFEF4444, 0xFFEC4899),
            voiceIntroDurationSec = 24,
            isOnline = true
        )
    )

    val initialMessages = mapOf(
        "user_seraphina" to listOf(
            ChatMessage(
                id = "m1",
                matchId = "user_seraphina",
                senderId = "user_seraphina",
                text = "Hello cosmic soul! ✨ Your resonance on cosmology and tea rituals caught my eye right away.",
                timestamp = "8:42 PM",
                isFromMe = false
            ),
            ChatMessage(
                id = "m2",
                matchId = "user_seraphina",
                senderId = "me",
                text = "Hey Seraphina! That 98% soul synergy score wasn't lying. What's the latest mystery you've been pondering?",
                timestamp = "8:45 PM",
                isFromMe = true
            ),
            ChatMessage(
                id = "m3",
                matchId = "user_seraphina",
                senderId = "user_seraphina",
                text = "Whether time is cyclical and if we have met before in a different iteration of stars. Would love to hop on a voice or video call whenever you're free!",
                timestamp = "8:47 PM",
                isFromMe = false
            )
        ),
        "user_kiran" to listOf(
            ChatMessage(
                id = "k1",
                matchId = "user_kiran",
                senderId = "user_kiran",
                text = "Hey there! I just finished composing an ambient sound piece inspired by dusk rain. Felt like something you'd appreciate.",
                timestamp = "Yesterday",
                isFromMe = false
            ),
            ChatMessage(
                id = "k2",
                matchId = "user_kiran",
                senderId = "me",
                text = "That sounds ethereal Kiran! Audio notes and rain are the ultimate cozy vibe.",
                timestamp = "Yesterday",
                isFromMe = true
            )
        )
    )

    val sampleThreads = listOf(
        SoulMatchThread(
            user = sampleUsers[0],
            lastMessage = "Would love to hop on a voice or video call whenever you're free!",
            lastMessageTime = "8:47 PM",
            unreadCount = 1,
            isConnected = true
        ),
        SoulMatchThread(
            user = sampleUsers[1],
            lastMessage = "Audio notes and rain are the ultimate cozy vibe.",
            lastMessageTime = "Yesterday",
            unreadCount = 0,
            isConnected = true
        ),
        SoulMatchThread(
            user = sampleUsers[2],
            lastMessage = "Connected via Soul Synergy Quiz (91%)",
            lastMessageTime = "Oct 25",
            unreadCount = 0,
            isConnected = true
        )
    )

    val quizQuestions = listOf(
        VibeQuizQuestion(
            id = 1,
            question = "On a rainy Sunday evening, how does your ideal soul connection unfold?",
            options = listOf(
                VibeOption("Curled up listening to vinyl records, talking until 2 AM about our secrets", "Cosmic Mystic", "+15% Emotional Synergy"),
                VibeOption("Cooking a warm nourishing meal together with candles and quiet jazz", "Sacred Architect", "+12% Lifestyle Harmony"),
                VibeOption("Taking a walk in the drizzle, sharing an umbrella and breathing the damp air", "Resonant Creator", "+14% Intuitive Bond"),
                VibeOption("Reading books back-to-back with our feet touching in peaceful silence", "Empathetic Harmonizer", "+16% Peaceful Resonance")
            )
        ),
        VibeQuizQuestion(
            id = 2,
            question = "When your heart is feeling heavy, what do you need most from a partner?",
            options = listOf(
                VibeOption("A quiet, non-judgmental presence that simply holds space without rushing to fix it", "Empathetic Harmonizer", "+18% Heart Sanctuary"),
                VibeOption("Deep philosophical conversation that helps put life into perspective", "Cosmic Mystic", "+15% Intellectual Anchor"),
                VibeOption("A tender physical embrace and a warm cup of herbal tea", "Sacred Architect", "+14% Comfort Alignment"),
                VibeOption("Gentle humor and an encouraging reminder of my inner power", "Luminous Nomad", "+13% Spiritual Lift")
            )
        ),
        VibeQuizQuestion(
            id = 3,
            question = "How do you define the sensation of encountering a true soulmate?",
            options = listOf(
                VibeOption("Instant familiarity — feeling like I've known them for a thousand lifetimes", "Cosmic Mystic", "+20% Eternal Affinity"),
                VibeOption("A wave of calm where all my nervous defenses immediately melt away", "Empathetic Harmonizer", "+18% Nervous System Serenity"),
                VibeOption("An electric spark of shared curiosity, creative inspiration, and laughter", "Resonant Creator", "+16% Electric Spark"),
                VibeOption("Unconditional mutual respect and an effortless alignment of values", "Sacred Architect", "+17% Foundation Harmony")
            )
        ),
        VibeQuizQuestion(
            id = 4,
            question = "What is the love language your soul speaks when nobody is watching?",
            options = listOf(
                VibeOption("Intimate voice calls late at night sharing raw unedited thoughts", "Resonant Creator", "+18% Vocal Intimacy"),
                VibeOption("Eyes locking across a crowded room with unspoken understanding", "Cosmic Mystic", "+16% Gaze Resonance"),
                VibeOption("Surprise little notes and handwritten poems tucked into books", "Empathetic Harmonizer", "+15% Devotion Tokens"),
                VibeOption("Spontaneous adventures under star-studded skies with no map", "Luminous Nomad", "+17% Cosmic Freedom")
            )
        )
    )
}
