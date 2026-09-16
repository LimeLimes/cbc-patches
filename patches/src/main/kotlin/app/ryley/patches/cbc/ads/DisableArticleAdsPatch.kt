package app.ryley.patches.cbc.ads

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.ryley.patches.cbc.shared.Constants.COMPATIBILITY_CBC_NEWS

/**
 * Removes the ads the app splices into article bodies.
 *
 * Article HTML (Polopoly CMS or GraphQL) is parsed and `[INSERTED_AD]` markers are inserted between
 * paragraphs whenever `InReadAdConfig.isAdsEnabled()` is true. Returning false from that getter
 * leaves the article markup untouched, so no in-article ad slot is ever created — this is the
 * article-view "web" ad surface, killed before any ad HTML exists.
 */
@Suppress("unused")
val disableArticleAdsPatch = bytecodePatch(
    name = "Disable article ads",
    description = "Prevents ads from being inserted into article bodies.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_CBC_NEWS)

    execute {
        InReadAdsEnabledFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x0
                return v0
            """,
        )
    }
}
