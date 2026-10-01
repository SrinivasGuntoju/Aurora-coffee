package com.example.data.repository

import com.example.R
import com.example.data.model.*

object CoffeeDataRepository {

    val signatureProducts = listOf(
        CoffeeProduct(
            id = "aurora-espresso",
            name = "AURORA ESPRESSO",
            price = 349,
            tag = "Flagship Reserve",
            roastLevel = "Medium-Dark",
            origin = "Sidama, Ethiopia & Cerrado, Brazil",
            notes = listOf("Dark Chocolate", "Hazelnut Praline", "Candied Orange"),
            description = "Our signature espresso blend engineered for silky mouthfeel, dense golden crema, and balanced cacao richness. Micro-roasted in small batches to preserve sweet aroma.",
            rating = 4.95,
            reviewsCount = 482,
            elevation = "1,950m - 2,200m",
            process = "Washed & Natural Anaerobic",
            imageResId = R.drawable.img_coffee_bag_pack,
            category = ProductCategory.COFFEE,
            isBestSeller = true
        ),
        CoffeeProduct(
            id = "midnight-roast",
            name = "MIDNIGHT ROAST",
            price = 399,
            tag = "Intense & Bold",
            roastLevel = "Dark",
            origin = "Aceh Highlands, Sumatra",
            notes = listOf("Smoked Molasses", "Black Truffle", "Toasted Walnut"),
            description = "Deep, velvety and unapologetically bold. Slow flame-roasted at 224°C to release natural caramelized sugars and heavy, syrupy body with zero astringency.",
            rating = 4.88,
            reviewsCount = 319,
            elevation = "1,600m",
            process = "Wet-Hulled (Giling Basah)",
            imageResId = R.drawable.img_coffee_bag_pack,
            category = ProductCategory.COFFEE,
            isBestSeller = true
        ),
        CoffeeProduct(
            id = "caramel-crema",
            name = "CARAMEL CREMA",
            price = 449,
            tag = "Connoisseur's Choice",
            roastLevel = "Medium",
            origin = "Huila, Colombia",
            notes = listOf("Dulce de Leche", "Brown Butter", "Vanilla Bean"),
            description = "An extraordinarily lush single-estate Colombian harvest. Notes of melted caramel and toasted macadamia that shine exquisitely in espresso and milk pours alike.",
            rating = 4.98,
            reviewsCount = 560,
            elevation = "1,850m",
            process = "Honey Processed 72hr",
            imageResId = R.drawable.img_coffee_bag_pack,
            category = ProductCategory.COFFEE,
            isBestSeller = true
        ),
        CoffeeProduct(
            id = "ethiopian-bloom",
            name = "ETHIOPIAN BLOOM",
            price = 499,
            tag = "Ultra Rare Lot",
            roastLevel = "Light-Medium",
            origin = "Yirgacheffe, Gedeb",
            notes = listOf("Jasmine Blossom", "Wild Bergamot", "Ripe Blueberry"),
            description = "Highland heirloom varietals grown above 2,100 meters. Opens with intoxicating floral perfumes followed by vibrant berry sweetness and tea-like elegance.",
            rating = 4.92,
            reviewsCount = 274,
            elevation = "2,150m",
            process = "Slow Dry Raised African Beds",
            imageResId = R.drawable.img_coffee_bag_pack,
            category = ProductCategory.COFFEE,
            isBestSeller = false
        )
    )

    val equipmentAndAccessories = listOf(
        CoffeeProduct(
            id = "aurora-precision-scale",
            name = "AURORA PRECISION SCALE",
            price = 2499,
            tag = "Brewmaster Gear",
            roastLevel = "Matte Black",
            origin = "Precision Engineered",
            notes = listOf("0.1g Accuracy", "Flow-Rate Timer", "Silicone Thermal Mat"),
            description = "Ultra-responsive touch capacitive brew scale with automatic flow-rate tracking and spill-resistant anodized aluminum base.",
            rating = 4.9,
            reviewsCount = 145,
            elevation = "Studio Grade",
            process = "Anodized Aerospace Aluminum",
            imageResId = R.drawable.img_espresso_crema_cup,
            category = ProductCategory.EQUIPMENT
        ),
        CoffeeProduct(
            id = "matte-ceramic-cup",
            name = "AURORA ARTISANAL CUP SET",
            price = 1299,
            tag = "Handcrafted",
            roastLevel = "Charcoal & Gold Rim",
            origin = "Kyoto Inspired Studio",
            notes = listOf("Double Wall Insulated", "Ergonomic Lip", "220ml Capacity"),
            description = "Tactile hand-thrown ceramic cups finished with a matte textured glaze and real gold leaf detailing. Enhances aroma dispersion.",
            rating = 4.97,
            reviewsCount = 208,
            elevation = "Kiln Fired",
            process = "Hand Thrown Stoneware",
            imageResId = R.drawable.img_espresso_crema_cup,
            category = ProductCategory.MUGS
        ),
        CoffeeProduct(
            id = "aurora-gift-trunk",
            name = "THE FOUNDER'S GIFT CHEST",
            price = 3499,
            tag = "Luxury Edition",
            roastLevel = "Curated Selection",
            origin = "Worldwide Harvest",
            notes = listOf("3 Reserve Coffees", "Tasting Journal", "Gold Coffee Scoop"),
            description = "Presented in a handcrafted dark walnut keepsake box with brushed brass hardware. Contains 3 limited harvest single origins and accessories.",
            rating = 5.0,
            reviewsCount = 94,
            elevation = "Gift Masterpiece",
            process = "Hand Packaged",
            imageResId = R.drawable.img_coffee_bag_pack,
            category = ProductCategory.GIFT_BOXES
        )
    )

    val allProducts: List<CoffeeProduct> = signatureProducts + equipmentAndAccessories

    val journeyStages = listOf(
        JourneyStage(
            number = "01",
            title = "SELECT",
            subtitle = "Sovereign High-Altitude Terroirs",
            description = "We hand-select only the top 1% specialty grade Arabica cherries harvested between 1,800m and 2,200m altitude. Hand-picked at peak brix sugar density.",
            keyMetric = "2,150 MASL",
            metricLabel = "Mean Harvest Elevation",
            sensoryProfile = "Dense Sugar Concentration • Jasmine Fragrance",
            imageResId = R.drawable.img_coffee_roast_craft
        ),
        JourneyStage(
            number = "02",
            title = "ROAST",
            subtitle = "Precision Drum Thermodynamics",
            description = "Slow micro-batch roasting inside dual-walled cast iron drums. Custom convection profiles calibrate caramelization down to a tenth of a second.",
            keyMetric = "204.5 °C",
            metricLabel = "First Crack Thermal Zenith",
            sensoryProfile = "Cacao Butter • Toasted Hazelnut • Molasses Core",
            imageResId = R.drawable.img_coffee_roast_craft
        ),
        JourneyStage(
            number = "03",
            title = "GRIND",
            subtitle = "Micron Particle Uniformity",
            description = "Titanium-coated conical burrs mill each roasted bean with zero thermal degradation, achieving uniform extraction surface geometry.",
            keyMetric = "250 µm",
            metricLabel = "Micro-calibrated Burrs",
            sensoryProfile = "Optimal Surface Area • Crystal Clarity",
            imageResId = R.drawable.img_coffee_bag_pack
        ),
        JourneyStage(
            number = "04",
            title = "BREW",
            subtitle = "Hydraulic Pressure & Thermal Equilibrium",
            description = "Purified spring water stabilized precisely at 92.8°C is delivered at 9 bars of hydraulic pressure for exactly 27 seconds of liquid alchemy.",
            keyMetric = "9.0 BAR",
            metricLabel = "Extraction Hydraulic Force",
            sensoryProfile = "Velvet Crema • Hydrophobic Lipid Emulsion",
            imageResId = R.drawable.img_hero_coffee_cube
        ),
        JourneyStage(
            number = "05",
            title = "ENJOY",
            subtitle = "The Unforgettable Sensory Reverie",
            description = "The culmination of patience. Multi-layered aroma waves unfold from initial citrus-floral brightness into lingering Madagascar vanilla finish.",
            keyMetric = "3-4 MIN",
            metricLabel = "Palate Finish Persistence",
            sensoryProfile = "Opulent Mouthfeel • Lingering Golden Sweetness",
            imageResId = R.drawable.img_espresso_crema_cup
        )
    )

    val beanOptions = listOf(
        BeanOption("ethiopia", "Ethiopia Yirgacheffe", "Gedeb Highland", "2,150m", "Floral Jasmine, Bergamot, Blueberry", 0.9f, 0.7f, 0.4f),
        BeanOption("colombia", "Colombia Huila Supremo", "Andean Slopes", "1,850m", "Dulce de Leche, Red Apple, Milk Chocolate", 0.6f, 0.85f, 0.7f),
        BeanOption("sumatra", "Sumatra Mandheling", "Lake Toba", "1,600m", "Smoked Cedar, Dark Cacao, Sweet Earth", 0.3f, 0.5f, 0.95f),
        BeanOption("reserve", "Aurora Signature Blend", "Multi-Continent Master Lot", "2,000m", "Caramelized Hazelnut, Dark Cocoa, Orange Zest", 0.65f, 0.9f, 0.85f)
    )

    val roastOptions = listOf(
        RoastOption("light", "Light Cinnamon", "196°C", "Crisp Brightness & High Floral Acidity", 0xFF8A5D3B),
        RoastOption("medium", "Medium Caramel", "208°C", "Peak Sweetness, Honey & Cream Body", 0xFF5C361E),
        RoastOption("dark", "Dark Velvet", "220°C", "Rich Cacao, Low Acidity, Heavy Body", 0xFF351C0F),
        RoastOption("midnight", "Midnight Smoke", "226°C", "Intense Smoky Molasses & Bold Finish", 0xFF1E0E06)
    )

    val grindOptions = listOf(
        GrindOption("whole", "Whole Bean", "Original", "Preserves freshness up to 90 days"),
        GrindOption("coarse", "Coarse Grind", "800µm", "Ideal for Cold Brew & French Press"),
        GrindOption("medium", "Medium Grind", "500µm", "Optimized for Chemex & V60 Pour Over"),
        GrindOption("fine", "Fine Espresso", "250µm", "Tight compaction for 9-bar espresso machines"),
        GrindOption("superfine", "Turkish Dust", "100µm", "Velvety micro-powder for ibrik extraction")
    )

    val brewOptions = listOf(
        BrewOption("espresso", "Espresso 9-Bar", "27 sec", "9.0 Bar", "Dense Golden Crema"),
        BrewOption("chemex", "Chemex Pour-Over", "3.5 min", "Atmospheric", "Crystal Clear Body"),
        BrewOption("aeropress", "Aeropress Immersion", "2 min", "Manual Plunge", "Rich & Smooth Texture"),
        BrewOption("colddrip", "Slow Cold Drip Tower", "12 hours", "Gravity Drip", "Liqueur-like Sweetness")
    )

    val sizeOptions = listOf(
        SizeOption("single", "Single Origin (150ml)", "Single Cup", 1.0),
        SizeOption("double", "Connoisseur Double (250ml)", "Double Pour", 1.4),
        SizeOption("grande", "Artisan Grande (350ml)", "Generous Craft", 1.8),
        SizeOption("flight", "Tasting Flight (3 x 100ml)", "Tasting Set", 2.2)
    )

    val subscriptionPlans = listOf(
        SubscriptionPlan(
            id = "weekly",
            name = "WEEKLY RESERVE",
            priceInr = 1199,
            period = "per month (4 bags)",
            subtitle = "For the passionate daily coffee devotee",
            perks = listOf(
                "4 x 250g bags freshly roasted each Monday",
                "Free priority refrigerated courier delivery",
                "Complimentary Aurora Cupping Spoon on sign-up",
                "Pause or modify roast preferences anytime"
            ),
            isPopular = false
        ),
        SubscriptionPlan(
            id = "monthly",
            name = "MONTHLY CURATOR",
            priceInr = 449,
            period = "per month (1 bag)",
            subtitle = "Rotating seasonal harvest selection",
            perks = listOf(
                "1 x 250g rare micro-lot shipped within 48h of roast",
                "Detailed tasting notes & roaster origin card",
                "15% VIP discount on all coffee equipment",
                "Zero commitment, cancel anytime with 1-tap"
            ),
            isPopular = true
        ),
        SubscriptionPlan(
            id = "premium",
            name = "FOUNDER'S CLUB",
            priceInr = 2299,
            period = "per month (Elite Reserve)",
            subtitle = "Access to unreleased competition micro-lots",
            perks = listOf(
                "2 x 250g Geisha / Experimental Anaerobic lots",
                "Annual invitation to Masterclass with Head Roaster",
                "Personalized monogrammed brass coffee container",
                "Private WhatsApp concierge with Q-Grader"
            ),
            isPopular = false
        )
    )

    val testimonials = listOf(
        Testimonial(
            id = "1",
            author = "Vikramaditya S.",
            role = "Master Sommelier",
            quote = "One of the smoothest, most transcendent espressos I have ever tasted in 15 years in hospitality. The hazelnut crema is astonishing.",
            rating = 5
        ),
        Testimonial(
            id = "2",
            author = "Elena Rostova",
            role = "Architect & Design Critic",
            quote = "Every single detail feels extraordinary — from the unboxing aroma to the subtle caramel finish. Aurora has redefined luxury coffee.",
            rating = 5
        ),
        Testimonial(
            id = "3",
            author = "Dr. Rajesh K.",
            role = "Coffee Connoisseur",
            quote = "The Ethiopian Bloom pour-over notes of jasmine and wild berries made my morning ritual feel like high art. An absolute masterpiece.",
            rating = 5
        )
    )
}
