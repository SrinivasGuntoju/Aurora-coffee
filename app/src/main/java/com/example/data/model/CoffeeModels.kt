package com.example.data.model

import com.example.R

enum class ProductCategory(val displayName: String) {
    COFFEE("Coffee"),
    EQUIPMENT("Equipment"),
    MUGS("Mugs"),
    GIFT_BOXES("Gift Boxes"),
    SUBSCRIPTIONS("Subscriptions")
}

data class CoffeeProduct(
    val id: String,
    val name: String,
    val price: Int, // In INR (₹)
    val tag: String,
    val roastLevel: String, // Light, Medium, Dark
    val origin: String,
    val notes: List<String>,
    val description: String,
    val rating: Double,
    val reviewsCount: Int,
    val elevation: String,
    val process: String,
    val imageResId: Int = R.drawable.img_coffee_bag_pack,
    val category: ProductCategory = ProductCategory.COFFEE,
    val isBestSeller: Boolean = false
)

data class CartItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val product: CoffeeProduct,
    val weight: String = "250g",
    val grind: String = "Whole Bean",
    val quantity: Int = 1,
    val customNotes: String = ""
) {
    val totalPrice: Int
        get() {
            val weightMultiplier = when (weight) {
                "500g" -> 1.85
                "1kg" -> 3.4
                else -> 1.0
            }
            return (product.price * weightMultiplier * quantity).toInt()
        }
}

data class BeanOption(
    val id: String,
    val name: String,
    val region: String,
    val elevation: String,
    val notes: String,
    val baseAcidity: Float,
    val baseSweetness: Float,
    val baseBody: Float
)

data class RoastOption(
    val id: String,
    val name: String,
    val tempC: String,
    val profile: String,
    val colorHex: Long
)

data class GrindOption(
    val id: String,
    val name: String,
    val microns: String,
    val bestFor: String
)

data class BrewOption(
    val id: String,
    val name: String,
    val time: String,
    val pressure: String,
    val cremaType: String
)

data class SizeOption(
    val id: String,
    val name: String,
    val volume: String,
    val priceMultiplier: Double
)

data class CoffeeLabConfig(
    val bean: BeanOption,
    val roast: RoastOption,
    val grind: GrindOption,
    val brew: BrewOption,
    val size: SizeOption
) {
    val estimatedPrice: Int
        get() = (390 * size.priceMultiplier).toInt()

    val acidityScore: Float
        get() = when (roast.id) {
            "light" -> 0.9f
            "medium" -> 0.65f
            "dark" -> 0.35f
            else -> 0.2f
        }

    val bodyScore: Float
        get() = when (roast.id) {
            "light" -> 0.4f
            "medium" -> 0.7f
            "dark" -> 0.95f
            else -> 0.85f
        }

    val sweetnessScore: Float
        get() = when (roast.id) {
            "light" -> 0.7f
            "medium" -> 0.9f
            "dark" -> 0.5f
            else -> 0.4f
        }

    val aromaIntensity: Float
        get() = 0.88f
}

data class JourneyStage(
    val number: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val keyMetric: String,
    val metricLabel: String,
    val sensoryProfile: String,
    val imageResId: Int
)

data class SubscriptionPlan(
    val id: String,
    val name: String,
    val priceInr: Int,
    val period: String,
    val subtitle: String,
    val perks: List<String>,
    val isPopular: Boolean = false
)

data class Testimonial(
    val id: String,
    val author: String,
    val role: String,
    val quote: String,
    val rating: Int = 5,
    val verified: Boolean = true
)
