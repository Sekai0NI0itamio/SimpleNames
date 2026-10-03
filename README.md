# SimpleNames

Per-player name colors for **Forge 1.20.1** servers. Server-side only, nothing
needed on clients (vanilla scoreboard teams already sync colors).

## Commands

- `/namecolour <color>` (or `/namecolor`) — color your own name
- `/namecolour clear` — back to default
- `/namecolour set <player> <color>` — ops only

Colors: aqua, black, blue, dark_aqua, dark_blue, dark_gray, dark_green,
dark_purple, dark_red, gold, gray, green, light_purple, red, white, yellow.

Colors persist in `<world>/serverconfig/simplenames.json` and reapply on login.

## Building

CI-only: every push runs `gradle build` (Java 17, unit tests) on GitHub
Actions and uploads the jar. Tags `v*` publish a release.
