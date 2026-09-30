# C4 Framework Constitution

Version: 0.1.0 · Status: Draft · Change control: ADR required (see §10)

This document is the highest authority in the repo. If code, a prompt, or an
AI agent's output conflicts with it, the constitution wins.

## 1. Purpose

A spec-driven framework that converts between the four C4 levels in both
directions, for Java applications first:

    C1 Context <-> C2 Container <-> C3 Component <-> C4 Code

Specs are the source of truth. Code and diagrams are projections of specs.
Customisation happens by extending specs, never by forking the core.

## 2. Principles

1. **Deterministic first, LLM second.** If a parser, build tool, or static
   analyser can produce a fact, the LLM must not guess it.
2. **Contracts before features.** Schemas, plugin interfaces, and tests are
   written before the behaviour they govern.
3. **Every element is traceable.** No spec element exists without a stable ID
   and valid links.
4. **Regenerate, don't overwrite.** Manual work is protected; drift is
   reported, never silently resolved.
5. **Small and diffable.** Specs are small files, one concern each, stable
   ordering, human-reviewable in a pull request.
6. **Core stays generic.** Java, Spring, and org-specific rules live in
   profiles and extensions, not in core.
7. **Prove it with evidence.** A capability is done only when a test or eval
   demonstrates it.

## 3. Deterministic vs LLM boundary

| Concern | Owner |
|---|---|
| Packages, classes, methods, annotations, dependencies | Deterministic extractor (JavaParser, build graph) |
| Modules, deployables, datastores, queues (from build/config) | Deterministic extractor |
| Schema validity, ID uniqueness, link completeness | Deterministic validator |
| Boundary rules (ArchUnit) | Generated from spec, executed deterministically |
| Names, summaries, descriptions, rationale | LLM |
| Grouping ambiguous classes into components (proposal only) | LLM, must be confirmed by rule or human |
| Business context, actors, external systems intent | Human, LLM may draft |
| Code skeletons from C3/C4 specs | Template-driven generator; LLM fills only marked slots |

Rule: an LLM output that adds a structural element (a container, component,
class, or dependency) not backed by an extractor result or an explicit human
input must be rejected by the validator.

## 4. Identity and traceability

- ID format: `<KIND>-<NNN>`; kinds are `SYS` (C1), `CTR` (C2), `CMP` (C3),
  `CLS` (C4). Actors: `ACT`. External systems: `EXT`.
- IDs are **stable**: once assigned they never change and are never reused.
  Renames change `name`, not `id`.
- Element ID assignment is deterministic and recorded in
  `trace/id-registry.yaml` (natural key -> ID). A natural key for code is the
  fully qualified name; for containers it is the module coordinate.
- Required links on every element:
  - `parent`: the containing element one level up (except C1 roots).
  - `realizes`: the element(s) one level up this element implements.
  - `source`: file/package/module location (mandatory for levels
    extracted from code; may be `null` only for C1 and `intent` elements).
- Provenance: every field records `origin: extracted | inferred | manual`.
  `inferred` (LLM) fields carry a `confidence` and are reviewable.

## 5. Generated vs manual content

- Spec regions are marked `generated` or `manual` (front-matter or block
  markers, defined in the spec schema).
- A regeneration run may overwrite `generated` regions only.
- A reverse run against an existing spec produces a **drift report**
  (added / removed / changed elements), never an in-place rewrite of `manual`
  regions.
- Gaps that cannot be inferred are recorded as `intent_gap` entries, not
  fabricated.

## 6. Layered resolution

Configuration, schemas, rules, and templates resolve in this order (later
layers override earlier ones):

    core -> java-profile -> extension(s) -> project

- Extensions declare a manifest (see `EXTENSION_CONTRACT.md`).
- Overrides are explicit and logged; a run prints the effective resolved
  configuration on request.
- Core never imports from a profile or extension.

## 7. Quality gates

A change is mergeable only if all gates pass:

1. **Schema:** all specs validate against versioned JSON Schemas.
2. **Links:** IDs unique, `parent`/`realizes` resolve, no orphans.
3. **Compile:** generated Java compiles.
4. **Boundaries:** generated ArchUnit rules pass.
5. **Golden:** output matches golden fixtures, or the golden update is
   explained in the PR.
6. **Round-trip:** extract -> generate -> extract yields no unexplained drift.
7. **Eval:** LLM skill outputs meet thresholds (schema-valid, no invented
   structure, links complete).

## 8. Skills

- Each level transition is a skill with a direction:
  `c1-to-c2`, `c2-to-c1`, `c2-to-c3`, `c3-to-c2`, `c3-to-c4`, `c4-to-c3`.
- Every skill directory contains: `SKILL.md`, input/output schema refs,
  prompt templates, a validator, and golden examples.
- Skills declare which fields they may set with `origin: inferred`.
- Skills are versioned; breaking a skill's output schema requires an ADR.

## 9. Non-goals (v0.x)

- Round-tripping arbitrary hand-drawn diagrams.
- Inferring business intent from code alone.
- Non-Java languages (the plugin interface allows them later).
- Being a diagramming UI (we export to Structurizr DSL / LikeC4).

## 10. Change control

- Any change to this constitution, a schema's major version, the ID rules, or
  the extension contract requires an ADR in `docs/adr/`.
- ADR format: context, decision, consequences, alternatives considered.
- AI agents must read relevant ADRs before proposing changes and must not
  contradict an accepted ADR without proposing a superseding one.
