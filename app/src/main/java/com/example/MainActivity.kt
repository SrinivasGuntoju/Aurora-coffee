package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.*
import com.example.ui.theme.AuroraBackground
import com.example.ui.theme.AuroraTheme
import com.example.ui.util.rememberBean3DTransform
import com.example.ui.viewmodel.AuroraViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuroraTheme {
                AuroraApp()
            }
        }
    }
}

@Composable
fun AuroraApp(
    viewModel: AuroraViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    // Smooth transition from cinematic loading screen
    Crossfade(
        targetState = uiState.isLoading,
        label = "loadingTransition"
    ) { isLoading ->
        if (isLoading) {
            CinematicLoadingScreen(
                progress = uiState.loadingProgress,
                onEnterClick = { viewModel.dismissLoading() }
            )
        } else {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AuroraBackground)
                    .testTag("aurora_main_scaffold"),
                topBar = {
                    AuroraTopBar(
                        cartCount = uiState.cartItems.sumOf { it.quantity },
                        wishlistCount = uiState.wishlistIds.size,
                        isAmbientAudioActive = uiState.ambientAudioActive,
                        onToggleAmbientAudio = { viewModel.toggleAmbientAudio() },
                        onCartClick = { viewModel.openCart() },
                        activeTab = uiState.activeNavTab,
                        onTabClick = { tab ->
                            viewModel.setNavTab(tab)
                            coroutineScope.launch {
                                val targetScroll = when (tab) {
                                    "home" -> 0
                                    "bean" -> 600
                                    "journey" -> 1150
                                    "collection" -> 1800
                                    "video" -> 3200
                                    "lab" -> 3800
                                    "packaging" -> 4900
                                    "store" -> 5600
                                    "subscription" -> 6800
                                    "story" -> 7800
                                    else -> 0
                                }
                                scrollState.animateScrollTo(targetScroll)
                            }
                        }
                    )
                },
                containerColor = AuroraBackground
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        // 1. Full-screen Immersive Hero Section
                        HeroSection(
                            onExploreClick = {
                                viewModel.setNavTab("collection")
                                coroutineScope.launch { scrollState.animateScrollTo(1800) }
                            },
                            onShopNowClick = {
                                viewModel.setNavTab("store")
                                coroutineScope.launch { scrollState.animateScrollTo(5600) }
                            }
                        )

                        Spacer(modifier = Modifier.height(36.dp))

                        // 2. Interactive 3D Coffee Bean Section (Native 60FPS 3D Scene with Scroll Tracking)
                        val bean3DTransform by rememberBean3DTransform(
                            scrollState = scrollState,
                            sectionTriggerStart = 300,
                            sectionTriggerEnd = 1400
                        )

                        Interactive3DCoffeeBeanCanvas(
                            isSplit = uiState.isBeanSplit,
                            onToggleSplit = { viewModel.toggleBeanSplit() },
                            transform = bean3DTransform,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )

                        Spacer(modifier = Modifier.height(48.dp))

                        // 3. Coffee Journey: "FROM BEAN TO CUP"
                        CoffeeJourneySection(
                            activeStageIndex = uiState.activeJourneyStageIndex,
                            onStageSelected = { idx -> viewModel.setJourneyStageIndex(idx) }
                        )

                        Spacer(modifier = Modifier.height(48.dp))

                        // 4. Signature Coffee Collection
                        SignatureCoffeeCollection(
                            wishlistIds = uiState.wishlistIds,
                            onToggleWishlist = { id -> viewModel.toggleWishlist(id) },
                            onProductClick = { product -> viewModel.openProductDetail(product) },
                            onQuickAddToCart = { product ->
                                viewModel.addToCart(product = product, weight = "250g", grind = "Whole Bean", quantity = 1)
                            }
                        )

                        Spacer(modifier = Modifier.height(48.dp))

                        // 5. Immersive Coffee Video Section: "EVERY CUP HAS A STORY."
                        ImmersiveCoffeeVideoSection()

                        Spacer(modifier = Modifier.height(48.dp))

                        // 6. Interactive Coffee Lab: "BUILD YOUR COFFEE"
                        InteractiveCoffeeLab(
                            config = uiState.labConfig,
                            onSelectBean = { viewModel.selectLabBean(it) },
                            onSelectRoast = { viewModel.selectLabRoast(it) },
                            onSelectGrind = { viewModel.selectLabGrind(it) },
                            onSelectBrew = { viewModel.selectLabBrew(it) },
                            onSelectSize = { viewModel.selectLabSize(it) },
                            onAddToCart = { viewModel.addCustomLabCoffeeToCart() }
                        )

                        Spacer(modifier = Modifier.height(48.dp))

                        // 7. 3D Product Showcase: Floating Luxury Packaging
                        ProductShowcase3D()

                        Spacer(modifier = Modifier.height(48.dp))

                        // 8. Coffee Store: Full E-Commerce Catalog
                        CoffeeStoreSection(
                            searchQuery = uiState.searchQuery,
                            onSearchQueryChange = { viewModel.setSearchQuery(it) },
                            selectedCategory = uiState.selectedCategory,
                            onCategorySelect = { viewModel.setCategory(it) },
                            wishlistIds = uiState.wishlistIds,
                            onToggleWishlist = { viewModel.toggleWishlist(it) },
                            onProductClick = { viewModel.openProductDetail(it) },
                            onQuickAddToCart = { product ->
                                viewModel.addToCart(product = product, weight = "250g", grind = "Whole Bean", quantity = 1)
                            }
                        )

                        Spacer(modifier = Modifier.height(48.dp))

                        // 9. Subscriptions Section: "NEVER RUN OUT OF GREAT COFFEE."
                        SubscriptionSection(
                            onSubscribe = { plan -> viewModel.subscribeToPlan(plan) }
                        )

                        Spacer(modifier = Modifier.height(48.dp))

                        // 10. Brand Story: "WE DON'T JUST ROAST COFFEE. WE CREATE MOMENTS."
                        BrandStorySection()

                        Spacer(modifier = Modifier.height(48.dp))

                        // 11. Testimonials: Connoisseur Praise
                        TestimonialsSection()

                        Spacer(modifier = Modifier.height(48.dp))

                        // 12. Luxury Dark Footer
                        LuxuryFooter(
                            emailInput = uiState.newsletterEmail,
                            onEmailChange = { viewModel.setNewsletterEmail(it) },
                            isSubscribed = uiState.newsletterSubscribed,
                            onSubscribeClick = { viewModel.submitNewsletter() },
                            onNavClick = { tab ->
                                viewModel.setNavTab(tab)
                                coroutineScope.launch {
                                    val targetScroll = when (tab) {
                                        "collection" -> 1800
                                        "lab" -> 3800
                                        "subscription" -> 6800
                                        else -> 0
                                    }
                                    scrollState.animateScrollTo(targetScroll)
                                }
                            }
                        )
                    }
                }
            }

            // Cart Slide-in Sheet
            CartDrawerSheet(
                isOpen = uiState.isCartOpen,
                items = uiState.cartItems,
                isDiscountApplied = uiState.isDiscountApplied,
                discountPercent = uiState.discountPercent,
                discountCode = uiState.discountCode,
                onClose = { viewModel.closeCart() },
                onUpdateQuantity = { id, delta -> viewModel.updateCartQuantity(id, delta) },
                onRemoveItem = { id -> viewModel.removeCartItem(id) },
                onApplyPromoCode = { code -> viewModel.applyPromoCode(code) },
                onRemovePromoCode = { viewModel.removePromoCode() },
                onCheckout = {
                    viewModel.placeOrder()
                    viewModel.closeCart()
                }
            )

            // Product Detail Modal
            ProductDetailModal(
                product = uiState.selectedProductForDetail,
                isWishlisted = uiState.selectedProductForDetail?.let { uiState.wishlistIds.contains(it.id) } ?: false,
                onToggleWishlist = {
                    uiState.selectedProductForDetail?.let { viewModel.toggleWishlist(it.id) }
                },
                onClose = { viewModel.closeProductDetail() },
                onAddToCart = { prod, weight, grind, qty ->
                    viewModel.addToCart(
                        product = prod,
                        weight = weight,
                        grind = grind,
                        quantity = qty
                    )
                }
            )

            // Order Confirmation Dialog
            if (uiState.isOrderPlaced) {
                OrderConfirmationDialog(
                    orderId = uiState.lastOrderId,
                    onDismiss = { viewModel.resetOrderPlaced() }
                )
            }

            // Subscription Confirmation Dialog
            uiState.subscribedPlan?.let { plan ->
                SubscriptionSuccessDialog(
                    plan = plan,
                    onDismiss = { viewModel.dismissSubscriptionDialog() }
                )
            }
        }
    }
}
