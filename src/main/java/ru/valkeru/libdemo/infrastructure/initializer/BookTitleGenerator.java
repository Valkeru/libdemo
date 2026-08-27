package ru.valkeru.libdemo.infrastructure.initializer;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

@UtilityClass
public class BookTitleGenerator {

    private static final List<NameTemplate> templates = List.of(
        new NameTemplate(70, () -> "The " + adjective() + " " + noun()),
        new NameTemplate(65, () -> gerund() + " " + noun()),
        new NameTemplate(60, () -> preposition() + " the " + noun()),
        new NameTemplate(55, () -> "Chronicles of " + place()),
        new NameTemplate(50, () -> "The " + adjective() + " " + noun() + " of " + place()),
        new NameTemplate(45, () -> noun() + " of " + abstracts()),
        new NameTemplate(40, () -> "The " + noun() + " of " + name()),
        new NameTemplate(35, () -> name() + "'s " + noun()),
        new NameTemplate(30, () -> noun() + " and " + noun()),
        new NameTemplate(25, () -> "Beyond the " + noun()),
        new NameTemplate(20, () -> "Return to " + place()),
        new NameTemplate(15, () -> "The Last " + noun()),
        new NameTemplate(10, () -> "The " + adjective() + " Path"),
        new NameTemplate(5, () -> "Under the " + noun()),
        new NameTemplate(30, () -> adjective() + " " + noun()),
        new NameTemplate(10, () -> "My " + noun()),
        new NameTemplate(10, () -> "Our " + noun()),
        new NameTemplate(10, () -> "A " + noun()),
        new NameTemplate(15, () -> noun() + " in " + place()),
        new NameTemplate(15, () -> noun() + " from " + place()),
        new NameTemplate(40, BookTitleGenerator::noun),
        new NameTemplate(20, BookTitleGenerator::abstracts),
        new NameTemplate(10, BookTitleGenerator::place)
    );

    private static final int totalWeight;

    static {
        totalWeight = templates.stream()
            .mapToInt(NameTemplate::weight)
            .sum();
    }

    private static final Random random = new Random();

    public static String generate() {
        int probe = random.nextInt(totalWeight);
        for (NameTemplate template : templates) {
            probe -= template.weight;
            if (probe < 0) {
                return template.generator().get();
            }
        }

        throw new IllegalStateException("Invalid template weights");
    }

    private static final String[] ADJECTIVES = {
        "Ancient", "Beautiful", "Bitter", "Black", "Blessed",
        "Blue", "Bold", "Broken", "Burning", "Cold",
        "Crimson", "Dark", "Dead", "Deep", "Distant",
        "Divine", "Endless", "Empty", "Eternal", "Fading",
        "Fallen", "Far", "Final", "First", "Forgotten",
        "Forsaken", "Frozen", "Gentle", "Golden", "Grand",
        "Gray", "Great", "Green", "Hidden", "Holy",
        "Invisible", "Iron", "Last", "Little", "Lonely",
        "Long", "Lost", "Lucky", "Midnight", "Mighty",
        "Mysterious", "Noble", "Northern", "Old", "Open",
        "Peaceful", "Quiet", "Red", "Restless", "Royal",
        "Sacred", "Scarlet", "Secret", "Shattered", "Silent",
        "Silver", "Sleeping", "Small", "Snowy", "Southern",
        "Strange", "True", "Unknown", "Vanished", "White",
        "Wild", "Winter", "Wise", "Wooden", "Young",
        "Bright", "Bleak", "Hidden", "Lonely", "Enduring",
        "Forgotten", "Nameless", "Broken", "Fearless", "Golden",
        "Emerald", "Invisible", "Restless", "Silent", "Timeless",
        "Able", "Ageless", "Amber", "Autumn", "Awakened",
        "Bare", "Beloved", "Blazing", "Blooming", "Brave",
        "Calm", "Careful", "Celestial", "Charming", "Chosen",
        "Clear", "Clever", "Cloudy", "Coastal", "Common",
        "Constant", "Cursed", "Daring", "Delicate", "Desolate",
        "Dying", "Early", "Eastern", "Electric", "Enchanted",
        "Evening", "Faithful", "Familiar", "Fearless", "Flaming",
        "Flowing", "Fragile", "Fresh", "Glorious", "Growing",
        "Harsh", "Heavenly", "Hollow", "Honest", "Humble",
        "Hungry", "Icy", "Immortal", "Inner", "Invisible",
        "Joyful", "Kind", "Late", "Lively", "Living",
        "Majestic", "Modern", "Moonlit", "Mortal", "Nearest",
        "Patient", "Proud", "Pure", "Remote", "Rugged",
        "Rural", "Rusty", "Shining", "Single", "Solar",
        "Solid", "Steady", "Stormy", "Sunny", "Tender",
        "Tiny", "Urban", "Vacant", "Vast", "Victorious",
        "Vivid", "Warm", "Weary", "Western", "Wounded"
    };

    private static String adjective() {
        int place = random.nextInt(ADJECTIVES.length);

        return ADJECTIVES[place];
    }

    private static final String[] NOUNS = {
        "Bridge", "Castle", "Cathedral", "Child", "City",
        "Cliff", "Clock", "Cottage", "Court", "Crown",
        "Desert", "Door", "Dream", "Empire", "Family",
        "Father", "Field", "Forest", "Fortress", "Friend",
        "Garden", "Gate", "Girl", "Harbor", "Heart",
        "Hill", "Home", "Horse", "House", "Island",
        "Journey", "Judge", "King", "Kingdom", "Knight",
        "Lake", "Letter", "Library", "Man", "Market",
        "Mill", "Mirror", "Monastery", "Moon", "Morning",
        "Mother", "Mountain", "Night", "Ocean", "Palace",
        "Path", "People", "Prince", "Princess", "Queen",
        "River", "Road", "School", "Sea", "Ship",
        "Shore", "Sky", "Soldier", "Spring", "Star",
        "Station", "Street", "Summer", "Sun", "Temple",
        "Throne", "Tower", "Town", "Train", "Traveler",
        "Tree", "Valley", "Village", "Voice", "Wall",
        "Warrior", "Waterfall", "Way", "Wind", "Window",
        "Winter", "Woman", "World", "Writer", "Year",
        "Academy", "Anchor", "Angel", "Archive", "Artist",
        "Balcony", "Barn", "Beacon", "Bell", "Bench",
        "Book", "Bottle", "Branch", "Camp", "Canal",
        "Captain", "Carriage", "Cave", "Cellar", "Chapel",
        "Circle", "Citizen", "Compass", "Corner", "Country",
        "Creek", "Crew", "Crossing", "Dancer", "Diary",
        "Dock", "Eagle", "Engine", "Farm", "Farmer",
        "Feather", "Festival", "Fountain", "Gallery", "Ghost",
        "Glove", "Harp", "Hunter", "Inn", "Journal",
        "Key", "Lantern", "Lighthouse", "Messenger", "Museum",
        "Nest", "Orchard", "Painter", "Passenger", "Pilgrim",
        "Pocket", "Poet", "Port", "Prison", "Professor",
        "Refuge", "Sailor", "Scholar", "Settlement", "Shelf",
        "Signal", "Singer", "Smith", "Square", "Stable",
        "Statue", "Stranger", "Studio", "Tavern", "Theater",
        "Tunnel", "Uncle", "University", "Visitor", "Voyage",
        "Workshop", "Youth"
    };

    private static String noun() {
        int place = random.nextInt(NOUNS.length);

        return NOUNS[place];
    }

    private static final String[] ABSTRACTS = {
        "Ashes", "Beauty", "Belief", "Blood", "Chaos",
        "Courage", "Darkness", "Dawn", "Death", "Destiny",
        "Dreams", "Dust", "Faith", "Fate", "Fear",
        "Firelight", "Forgiveness", "Freedom", "Glory", "Grace",
        "Grief", "Harmony", "Hatred", "Honor", "Hope",
        "Ice", "Innocence", "Justice", "Kindness", "Knowledge",
        "Liberty", "Light", "Loneliness", "Love", "Luck",
        "Madness", "Mercy", "Mist", "Moonlight", "Music",
        "Nightfall", "Oblivion", "Pain", "Passion", "Peace",
        "Power", "Pride", "Rain", "Reason", "Redemption",
        "Regret", "Revenge", "Salvation", "Sand", "Silence",
        "Smoke", "Snowfall", "Sorrow", "Soul", "Spirit",
        "Springtime", "Starlight", "Steel", "Strength", "Summer",
        "Sunlight", "Thunder", "Time", "Truth", "Twilight",
        "Victory", "Violence", "War", "Warmth", "Water",
        "Wealth", "Whispers", "Wildness", "Wind", "Wisdom",
        "Wonder", "Youth",
        "Abundance", "Acceptance", "Adventure", "Ambition", "Balance",
        "Bliss", "Calmness", "Chance", "Charity", "Clarity",
        "Compassion", "Confidence", "Conflict", "Curiosity", "Decay",
        "Delight", "Despair", "Devotion", "Discovery", "Echoes",
        "Elegance", "Envy", "Equality", "Eternity", "Expectation",
        "Fortune", "Friendship", "Fury", "Generosity", "Guilt",
        "Healing", "History", "Humility", "Illusion", "Imagination",
        "Independence", "Infinity", "Insight", "Inspiration", "Integrity",
        "Joy", "Legacy", "Liberation", "Loss", "Loyalty",
        "Miracles", "Mystery", "Nature", "Opportunity", "Patience",
        "Perfection", "Persistence", "Purpose", "Reflection", "Resilience",
        "Respect", "Sacrifice", "Serenity", "Simplicity", "Sincerity",
        "Solitude", "Stability", "Survival", "Symmetry", "Tenderness",
        "Tradition", "Transformation", "Unity", "Valor", "Vision",
        "Vitality", "Vulnerability", "Will", "Wonderment", "Zeal"
    };

    private static String abstracts() {
        int place = random.nextInt(ABSTRACTS.length);

        return ABSTRACTS[place];
    }

    private static final String[] NAMES = {
        "Aaron", "Abigail", "Adam", "Adrian", "Alan",
        "Albert", "Alexander", "Alexandra", "Alexis", "Alice",
        "Alicia", "Amanda", "Amber", "Amelia", "Amy",
        "Andrea", "Andrew", "Angela", "Anna", "Anthony",
        "Arthur", "Ashley", "Audrey", "Austin", "Barbara",
        "Benjamin", "Bernard", "Beth", "Betty", "Blake",
        "Brandon", "Brenda", "Brian", "Brittany", "Bruce",
        "Caleb", "Calvin", "Cameron", "Carl", "Caroline",
        "Carol", "Catherine", "Charles", "Charlotte", "Cheryl",
        "Christian", "Christina", "Christopher", "Claire", "Clara",
        "Cole", "Colin", "Connor", "Courtney", "Crystal",
        "Daniel", "Danielle", "David", "Dean", "Deborah",
        "Dennis", "Derek", "Diana", "Dominic", "Donna",
        "Dorothy", "Douglas", "Dylan",
        "Edward", "Eleanor", "Elizabeth", "Ella", "Ellen",
        "Emily", "Emma", "Eric", "Erica", "Ethan",
        "Eugene", "Eva", "Evelyn",
        "Faith", "Felicia", "Florence", "Frances", "Frank",
        "Gabriel", "Gavin", "George", "Georgia", "Gerald",
        "Grace", "Gregory",
        "Hannah", "Harold", "Harry", "Hazel", "Heather",
        "Helen", "Henry", "Holly", "Howard",
        "Ian", "Isaac", "Isabel", "Isabella", "Ivan",
        "Jack", "Jacob", "James", "Jane", "Janet",
        "Jasmine", "Jason", "Jean", "Jeffrey", "Jennifer",
        "Jeremy", "Jerry", "Jesse", "Jessica", "Joan",
        "Joe", "John", "Johnny", "Jonathan", "Jordan",
        "Joseph", "Joshua", "Joyce", "Judith", "Julia",
        "Julian", "Julie", "Justin",
        "Karen", "Katherine", "Kathleen", "Kathryn", "Kayla",
        "Keith", "Kelly", "Kenneth", "Kevin", "Kimberly",
        "Kyle", "Laura", "Lauren", "Lawrence", "Leah", "Leo",
        "Leonard", "Lillian", "Lily", "Linda", "Lisa",
        "Logan", "Lori", "Louis", "Lucas", "Lucy",
        "Luke", "Lydia", "Madeline", "Madison", "Margaret", "Maria", "Marie",
        "Marilyn", "Mark", "Martha", "Martin", "Mary",
        "Mason", "Matthew", "Megan", "Melanie", "Melissa",
        "Michael", "Michelle", "Molly", "Morgan",
        "Nancy", "Natalie", "Nathan", "Nathaniel", "Neil",
        "Nicholas", "Nicole", "Noah", "Norman",
        "Olivia", "Oliver", "Oscar",
        "Pamela", "Patricia", "Patrick", "Paul", "Peter",
        "Philip", "Phillip", "Rachel", "Raymond", "Rebecca",
        "Richard", "Robert", "Roger", "Ronald", "Rose",
        "Roy", "Russell", "Ruth", "Ryan",
        "Sally", "Samantha", "Samuel", "Sandra", "Sara",
        "Sarah", "Scott", "Sean", "Sharon", "Shawn",
        "Sophia", "Spencer", "Stephanie", "Stephen", "Steven",
        "Susan", "Sydney",
        "Taylor", "Teresa", "Terry", "Thomas", "Tiffany",
        "Timothy", "Todd", "Tracy", "Travis", "Trevor",
        "Tyler", "Valerie", "Vanessa", "Victoria", "Vincent",
        "Virginia", "Walter", "Wayne", "Wendy", "William", "Willie",

        "Zachary", "Zoe"
    };

    private static String name() {
        int place = random.nextInt(NAMES.length);

        return NAMES[place];
    }

    private static final String[] PLACES = {
        "Amberfall", "Ashford", "Ashgrove", "Black Harbor",
        "Blackridge", "Blackwood", "Bluehaven", "Brighton",
        "Brookfield", "Clearwater", "Cold Harbor", "Deepwood",
        "Dragonstone", "Eastgate", "Eastwood", "Elderwood",
        "Fairview", "Fox Hollow", "Goldhaven", "Golden Hill",
        "Graymoor", "Greenhill", "Greyhaven", "Highgarden",
        "Highland", "Kingsbridge", "Kingsport", "Lakewood",
        "Maple Grove", "Millstone", "Moonvale", "Northgate",
        "Northwood", "Oakridge", "Oakvale", "Oldbridge",
        "Pinecrest", "Pinehurst", "Raven Hill", "Ravenwood",
        "Redcliff", "Redhill", "Riverbend", "Riverdale",
        "Rosewood", "Shadowbrook", "Silver Creek", "Silverhill",
        "Southgate", "Springfield", "Stonebridge", "Stonehaven",
        "Sunnydale", "Westbridge", "Westhaven", "Whitefield",
        "Willow Creek", "Winterbrook", "Winterfell", "Woodhaven",
        "Appleford", "Bear Creek", "Birch Hollow", "Bluewater",
        "Brimstone", "Cedar Falls", "Cinder Point", "Copperhill",
        "Crystal Bay", "Darkwater", "Deerfield", "Dry Creek",
        "Eagle Pass", "Elmwood", "Evergreen", "Falcon Ridge",
        "Fern Valley", "Fog Hollow", "Frostford", "Gold Creek",
        "Grand Oaks", "Greenford", "Hawthorne", "Hillcrest",
        "Ironford", "Ironhill", "Juniper", "Lakeside",
        "Longmeadow", "Meadowbrook", "Mistwood", "Mossfield",
        "Newhaven", "Oldstone", "Pine Valley", "Quartz Hill",
        "Red Creek", "Rockford", "Sandpoint", "Seacliff",
        "Snowridge", "Southport", "Stonefield", "Storm Bay",
        "Summerhill", "Sunset Harbor", "Timber Falls", "Twin Oaks",
        "White Harbor", "White Oak", "Wildbrook", "Windermere",
        "Winter Harbor", "Wolf Creek", "Woodbridge", "Woodcrest",
        "Woodhill", "Woodland", "Yellow Creek", "Yorkshire"
    };

    private static String place() {
        int place = random.nextInt(PLACES.length);

        return PLACES[place];
    }

    private static final String[] GERUNDS = {
        "Breaking", "Building", "Burning", "Calling",
        "Changing", "Chasing", "Crossing", "Discovering",
        "Dreaming", "Escaping", "Facing", "Falling",
        "Finding", "Following", "Forgiving", "Hearing",
        "Hiding", "Holding", "Keeping", "Knowing",
        "Leaving", "Losing", "Meeting", "Missing",
        "Passing", "Protecting", "Remembering", "Returning",
        "Rising", "Running", "Saving", "Searching",
        "Seeking", "Shaping", "Sharing", "Standing",
        "Surviving", "Traveling", "Trusting", "Waiting",
        "Walking", "Watching", "Winning", "Writing",
        "Accepting", "Answering", "Arriving", "Believing", "Breathing",
        "Collecting", "Coming", "Connecting", "Creating", "Dancing",
        "Deciding", "Defending", "Drawing", "Emerging", "Entering",
        "Exploring", "Fighting", "Flying", "Gathering", "Giving",
        "Growing", "Guiding", "Helping", "Hoping", "Imagining",
        "Journeying", "Learning", "Listening", "Living", "Looking",
        "Loving", "Moving", "Observing", "Opening", "Preparing",
        "Questioning", "Reading", "Reaching", "Recovering", "Reflecting",
        "Resting", "Sailing", "Shining", "Smiling", "Speaking",
        "Starting", "Studying", "Teaching", "Turning", "Understanding",
        "Uniting", "Visiting", "Wondering", "Working", "Yielding"
    };

    private static String gerund() {
        int place = random.nextInt(GERUNDS.length);

        return GERUNDS[place];
    }

    private static final String[] PREPOSITIONS = {
        "Above", "Across", "After", "Before",
        "Behind", "Beyond", "Between", "Inside",
        "Near", "Over", "Through", "Under"
    };

    private static String preposition() {
        int place = random.nextInt(PREPOSITIONS.length);

        return PREPOSITIONS[place];
    }

    record NameTemplate(int weight, Supplier<String> generator) {
    }
}
