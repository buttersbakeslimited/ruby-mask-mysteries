package com.Fidmowe7.Loskow.data.sample

import com.Fidmowe7.Loskow.R
import com.Fidmowe7.Loskow.domain.model.MissionId

object SampleData {

    val rivalNames = listOf(
        R.string.rival_carmine,
        R.string.rival_velvet_fox,
        R.string.rival_ashwood,
        R.string.rival_orris
    )

    val rivalScores = listOf(18400, 9720, 7310, 5180)

    val caseNames = listOf(
        R.string.case_ballroom,
        R.string.case_jewel_room,
        R.string.case_escape
    )

    val mapNodeNames = listOf(
        R.string.node_invitation,
        R.string.node_first_waltz,
        R.string.node_broken_clasp,
        R.string.node_mirror_hall,
        R.string.node_velvet_rope,
        R.string.node_servants_stair,
        R.string.node_jewel_room,
        R.string.node_unmasking
    )

    val missionTitles = mapOf(
        MissionId.SpinTwentyFive to R.string.mission_spin_25,
        MissionId.ThreeMasks to R.string.mission_three_masks,
        MissionId.ClueStreakFour to R.string.mission_clue_streak,
        MissionId.UnlockThreeTiles to R.string.mission_unlock_tiles,
        MissionId.VaultTwice to R.string.mission_vault_twice,
        MissionId.ClaimNightly to R.string.mission_claim_nightly
    )

    val rewardCredits = listOf(80, 0, 150, 0, 220, 0, 400)

    val rewardShards = listOf(0, 3, 0, 4, 0, 5, 6)

    val previewTitles = listOf(
        R.string.preview_mask_vault,
        R.string.preview_ruby_shards,
        R.string.preview_clue_streak,
        R.string.preview_nightly_vault
    )

    val previewRules = listOf(
        R.string.preview_rule_mask_vault,
        R.string.preview_rule_ruby_shards,
        R.string.preview_rule_clue_streak,
        R.string.preview_rule_nightly_vault
    )
}
