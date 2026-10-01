package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.CoffeeDataRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuroraUiState(
    val isLoading: Boolean = true,
    val loadingProgress: Float = 0f,
    val cartItems: List<CartItem> = listOf(),
    val wishlistIds: Set<String> = setOf("aurora-espresso", "ethiopian-bloom"),
    val searchQuery: String = "",
    val selectedCategory: ProductCategory? = null,
    val quickViewProduct: CoffeeProduct? = null,
    val selectedProductForDetail: CoffeeProduct? = null,
    val isCartOpen: Boolean = false,
    val discountCode: String = "",
    val isDiscountApplied: Boolean = false,
    val discountPercent: Int = 0,
    val isOrderPlaced: Boolean = false,
    val lastOrderId: String = "",
    val ambientAudioActive: Boolean = false,
    val beanSplitProgress: Float = 0f,
    val beanRotationAngle: Float = 0f,
    val isBeanSplit: Boolean = false,
    val videoPlaying: Boolean = true,
    val activeStoryIndex: Int = 0,
    val activeJourneyStageIndex: Int = 0,
    val labConfig: CoffeeLabConfig = CoffeeLabConfig(
        bean = CoffeeDataRepository.beanOptions[0],
        roast = CoffeeDataRepository.roastOptions[1],
        grind = CoffeeDataRepository.grindOptions[2],
        brew = CoffeeDataRepository.brewOptions[0],
        size = CoffeeDataRepository.sizeOptions[1]
    ),
    val subscribedPlan: SubscriptionPlan? = null,
    val newsletterEmail: String = "",
    val newsletterSubscribed: Boolean = false,
    val activeNavTab: String = "home"
)

class AuroraViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuroraUiState())
    val uiState: StateFlow<AuroraUiState> = _uiState.asStateFlow()

    init {
        startLoadingSequence()
    }

    private fun startLoadingSequence() {
        viewModelScope.launch {
            // Smooth cinematic loading progress
            for (i in 1..100) {
                delay(18)
                _uiState.update { it.copy(loadingProgress = i / 100f) }
            }
            delay(400)
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun dismissLoading() {
        _uiState.update { it.copy(isLoading = false) }
    }

    fun setNavTab(tab: String) {
        _uiState.update { it.copy(activeNavTab = tab) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setCategory(category: ProductCategory?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun openProductDetail(product: CoffeeProduct) {
        _uiState.update { it.copy(selectedProductForDetail = product) }
    }

    fun closeProductDetail() {
        _uiState.update { it.copy(selectedProductForDetail = null) }
    }

    fun openQuickView(product: CoffeeProduct) {
        _uiState.update { it.copy(quickViewProduct = product) }
    }

    fun closeQuickView() {
        _uiState.update { it.copy(quickViewProduct = null) }
    }

    fun openCart() {
        _uiState.update { it.copy(isCartOpen = true) }
    }

    fun closeCart() {
        _uiState.update { it.copy(isCartOpen = false) }
    }

    fun toggleCart() {
        _uiState.update { it.copy(isCartOpen = !it.isCartOpen) }
    }

    fun addToCart(
        product: CoffeeProduct,
        weight: String = "250g",
        grind: String = "Whole Bean",
        quantity: Int = 1,
        customNotes: String = ""
    ) {
        _uiState.update { state ->
            val existingIndex = state.cartItems.indexOfFirst {
                it.product.id == product.id && it.weight == weight && it.grind == grind
            }
            val updatedList = if (existingIndex != -1) {
                state.cartItems.toMutableList().apply {
                    val current = this[existingIndex]
                    this[existingIndex] = current.copy(quantity = current.quantity + quantity)
                }
            } else {
                state.cartItems + CartItem(
                    product = product,
                    weight = weight,
                    grind = grind,
                    quantity = quantity,
                    customNotes = customNotes
                )
            }
            state.copy(cartItems = updatedList, isCartOpen = true)
        }
    }

    fun updateCartQuantity(itemId: String, delta: Int) {
        _uiState.update { state ->
            val updated = state.cartItems.mapNotNull { item ->
                if (item.id == itemId) {
                    val newQty = item.quantity + delta
                    if (newQty > 0) item.copy(quantity = newQty) else null
                } else item
            }
            state.copy(cartItems = updated)
        }
    }

    fun removeCartItem(itemId: String) {
        _uiState.update { state ->
            state.copy(cartItems = state.cartItems.filterNot { it.id == itemId })
        }
    }

    fun toggleWishlist(productId: String) {
        _uiState.update { state ->
            val updated = if (state.wishlistIds.contains(productId)) {
                state.wishlistIds - productId
            } else {
                state.wishlistIds + productId
            }
            state.copy(wishlistIds = updated)
        }
    }

    fun applyPromoCode(code: String): Boolean {
        val trimmed = code.trim().uppercase()
        return if (trimmed == "AURORA15" || trimmed == "GOLDEN") {
            _uiState.update {
                it.copy(
                    discountCode = trimmed,
                    isDiscountApplied = true,
                    discountPercent = 15
                )
            }
            true
        } else {
            false
        }
    }

    fun removePromoCode() {
        _uiState.update {
            it.copy(
                discountCode = "",
                isDiscountApplied = false,
                discountPercent = 0
            )
        }
    }

    fun placeOrder(): String {
        val orderId = "AUR-" + (100000..999999).random()
        _uiState.update {
            it.copy(
                isOrderPlaced = true,
                lastOrderId = orderId,
                cartItems = emptyList()
            )
        }
        return orderId
    }

    fun resetOrderPlaced() {
        _uiState.update { it.copy(isOrderPlaced = false) }
    }

    // 3D Coffee Bean Interactions
    fun toggleBeanSplit() {
        _uiState.update { it.copy(isBeanSplit = !it.isBeanSplit) }
    }

    fun updateBeanRotation(deltaAngle: Float) {
        _uiState.update { it.copy(beanRotationAngle = (it.beanRotationAngle + deltaAngle) % 360f) }
    }

    // Coffee Lab Customizer
    fun selectLabBean(bean: BeanOption) {
        _uiState.update { it.copy(labConfig = it.labConfig.copy(bean = bean)) }
    }

    fun selectLabRoast(roast: RoastOption) {
        _uiState.update { it.copy(labConfig = it.labConfig.copy(roast = roast)) }
    }

    fun selectLabGrind(grind: GrindOption) {
        _uiState.update { it.copy(labConfig = it.labConfig.copy(grind = grind)) }
    }

    fun selectLabBrew(brew: BrewOption) {
        _uiState.update { it.copy(labConfig = it.labConfig.copy(brew = brew)) }
    }

    fun selectLabSize(size: SizeOption) {
        _uiState.update { it.copy(labConfig = it.labConfig.copy(size = size)) }
    }

    fun addCustomLabCoffeeToCart() {
        val config = _uiState.value.labConfig
        val customProduct = CoffeeProduct(
            id = "custom-lab-${System.currentTimeMillis()}",
            name = "CUSTOM: ${config.bean.name.uppercase()}",
            price = config.estimatedPrice,
            tag = "Lab Custom Creation",
            roastLevel = config.roast.name,
            origin = config.bean.region,
            notes = listOf(config.grind.name, config.brew.name, config.size.name),
            description = "Bespoke blend engineered in the Aurora Interactive Coffee Lab. Crafted with ${config.bean.name}, roasted to ${config.roast.name}, extracted via ${config.brew.name}.",
            rating = 5.0,
            reviewsCount = 1,
            elevation = config.bean.elevation,
            process = "Bespoke Precision Extraction",
            category = ProductCategory.COFFEE
        )
        addToCart(
            product = customProduct,
            weight = config.size.name,
            grind = config.grind.name,
            quantity = 1,
            customNotes = "Brew method: ${config.brew.name} | Roast: ${config.roast.name}"
        )
    }

    // Story / Video Reel controls
    fun toggleVideoPlayback() {
        _uiState.update { it.copy(videoPlaying = !it.videoPlaying) }
    }

    fun setStoryIndex(index: Int) {
        _uiState.update { it.copy(activeStoryIndex = index.coerceIn(0, 3)) }
    }

    fun setJourneyStageIndex(index: Int) {
        _uiState.update { it.copy(activeJourneyStageIndex = index.coerceIn(0, 4)) }
    }

    // Audio toggle
    fun toggleAmbientAudio() {
        _uiState.update { it.copy(ambientAudioActive = !it.ambientAudioActive) }
    }

    // Subscriptions
    fun subscribeToPlan(plan: SubscriptionPlan) {
        _uiState.update { it.copy(subscribedPlan = plan) }
    }

    fun dismissSubscriptionDialog() {
        _uiState.update { it.copy(subscribedPlan = null) }
    }

    // Newsletter
    fun setNewsletterEmail(email: String) {
        _uiState.update { it.copy(newsletterEmail = email) }
    }

    fun submitNewsletter(): Boolean {
        val email = _uiState.value.newsletterEmail.trim()
        return if (email.contains("@") && email.contains(".")) {
            _uiState.update { it.copy(newsletterSubscribed = true) }
            true
        } else {
            false
        }
    }
}
