# Criteria: SimpleNames (Forge 1.20.1 /namecolour mod)

- [ ] `gradle build` green on GitHub Actions (Java 17); nothing compiled locally
- [ ] CI uploads `simplenames-1.0.0.jar`; unit tests pass (color validation, team-name shape)
- [ ] `/namecolour <color>` colors the player's name via scoreboard teams; invalid names rejected with the color list
- [ ] `/namecolour clear` restores default; color persists across restarts and reapplies on login
- [ ] Server-only safe: no packets, no client registries (vanilla teams already sync)
