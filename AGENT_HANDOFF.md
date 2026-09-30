# AGENT HANDOFF: C4 Framework Build Plan

You are an AI engineer building a **spec-driven, bidirectional C4 framework for
Java applications**. Work step by step through the tasks below, one task per
session, in order. Do not skip ahead.

## 0. Read first (mandatory)

1. `CONSTITUTION.md`: highest authority. It overrides this file.
2. `CLAUDE.md`: working rules and quality gates.
3. `EXTENSION_CONTRACT.md`: extension model.
4. Any relevant ADR in `docs/adr/`.

For VS Code Copilot: copy the "Standing instructions" (section 1) into
`.github/copilot-instructions.md` so they apply to every chat.

## 1. Standing instructions (paste into copilot-instructions.md)

- Follow `CONSTITUTION.md` and `CLAUDE.md`. If instructions conflict, the
  constitution wins.
- Deterministic tools first (JavaParser, build graph, ArchUnit); LLM second.
- Never invent structure. Unknown facts become `intent_gap` entries.
- IDs are stable and never reused. `SYS/ACT/EXT/CTR/CMP/CLS` format.
- Every element has `parent`, `realizes`, `source`, and per-field `origin`.
- Edit only `generated` regions; never touch `manual` regions.
- No Java/Spring/org-specific logic in `core/`. Core never imports profiles or
  extensions.
- Write tests / golden fixtures before implementation.
- Run all applicable quality gates and show real output before saying "done".
- One narrow task per session; commit small: `<area>: <change>`.
- If unsure, ask a specific question or write an ADR proposal. Do not guess.

## 2. Session protocol (repeat for every task)

1. State the task ID and restate its acceptance criteria.
2. List the files you will create or change. Wait for no approval, but stay
   inside that list.
3. Write failing tests / golden fixtures first.
4. Implement the minimum to pass.
5. Run the gates: schemas, links, compile, boundaries, golden, round-trip,
   eval (those that exist by now).
6. Report: files changed, gate output, known gaps.
7. Update the task checklist in `docs/PROGRESS.md`.

## 3. Reference application

- Monolith: `spring-projects/spring-petclinic`
- Microservices: `spring-petclinic/spring-petclinic-microservices`

Add as git submodules under `examples/`. Verify the repo URLs before cloning.
Never modify the examples in place; generated output goes to `out/`.

## 4. Task list (ordered)

Each task ends only when its **Done when** items are verified.

### PHASE A: Foundations

**T01: Repo scaffold**
- Create layout: `core/{specs,skills,trace,plugin-api}`, `java-profile/`,
  `extensions/`, `examples/`, `evals/`, `docs/adr/`, `out/`.
- Add `Makefile` with placeholder targets for every gate in `CLAUDE.md`.
- Add `docs/PROGRESS.md` with this task list as checkboxes.
- Done when: `make` lists all targets; each runs and exits 0 with a
  "not implemented" notice.

**T02: ID and trace rules**
- Write `core/trace/RULES.md` and `core/trace/id-registry.schema.json`.
- Implement deterministic ID assignment: natural key -> ID, persisted in
  `trace/id-registry.yaml`, never reassigning.
- Done when: unit tests show same input -> same IDs; a rename keeps the ID;
  a deleted element's ID is never reused.

**T03: Level schemas v0.1**
- JSON Schemas: `context`, `container`, `component`, `code` in
  `core/specs/`. Required fields: `id`, `kind`, `name`, `parent`, `realizes`,
  `source`, `origin`, `regions`. Add `intent_gap` and `confidence` support.
- Add valid and invalid example specs per level as tests.
- Done when: `make validate-schemas` passes valid examples and rejects every
  invalid one.

**T04: Link validator**
- Implement checks: unique IDs, `parent` and `realizes` resolve, no orphans,
  level-appropriate parent kinds.
- Done when: fixtures with each defect type produce the expected finding.

**T05: Plugin API**
- Define interfaces `Extractor`, `Generator`, `Validator`, `Hook` per
  `EXTENSION_CONTRACT.md` §5, in `core/plugin-api/`.
- Done when: a stub no-op profile loads and runs through the pipeline.

### PHASE B: Reverse extraction (deterministic)

**T06: C4 code extractor (Java)**
- In `java-profile/`, use JavaParser to extract packages, classes,
  interfaces, annotations, imports, method signatures from the monolith.
- Emit C4 specs with stable IDs and `origin: extracted`.
- Done when: output is deterministic (two runs are byte-identical) and
  validates; class count matches an independent count.

**T07: C3 component grouping**
- Rule files for grouping classes into components (package + stereotype such
  as `@Controller`, `@Service`, `@Repository`, `@Entity`).
- Emit C3 specs with `realizes`/`parent` links to C4 elements.
- Done when: every C4 class belongs to exactly one component or is listed as
  ungrouped with a reason; links validate.

**T08: Boundary rules to ArchUnit**
- Generate ArchUnit tests from the C3 spec's allowed dependencies.
- Done when: generated tests compile and pass on the monolith.

**T09: C2 container extraction**
- Extract modules/deployables from Maven/Gradle, `application*.yml`, DB and
  messaging config, Dockerfiles.
- Done when: containers include app and datastore(s), with `source` pointing
  at the evidence files.

### PHASE C: LLM skills (reverse)

**T10: Skill template and eval harness**
- Create `core/skills/_template/` (SKILL.md, prompts/, validator, golden/).
- Build `evals/` runner: schema-valid, no invented structure, links complete.
- Done when: a dummy skill runs through the harness and reports scores.

**T11: `c4-to-c3` and `c3-to-c2` skills**
- LLM proposes names/summaries/rationale only; structure comes from
  extractors. Output must be validated and use `origin: inferred` with
  `confidence`.
- Done when: eval passes thresholds; a fixture with an injected fake class is
  rejected.

**T12: `c2-to-c1` skill**
- Draft SYS/ACT/EXT with `intent_gap` entries for anything not derivable.
- Export C1/C2 to Structurizr DSL (or LikeC4).
- Done when: exported DSL parses; every gap is explicit.

### PHASE D: Forward generation

**T13: `c1-to-c2` and `c2-to-c3` skills** (spec-to-spec)
- Done when: given the reverse-produced C1, forward output is structurally
  consistent with the extracted C2/C3 (diff report is empty or explained).

**T14: `c3-to-c4` generator**
- Template-driven Java skeletons (packages, interfaces, stubs, tests). LLM
  fills only declared slots.
- Done when: `make compile-generated` passes on the generated skeleton.

**T15: Round-trip**
- Pipeline: extract -> generate -> extract. Compare specs.
- Done when: `make test-roundtrip` passes, or every drift item is listed with
  an explanation and a backlog ticket.

### PHASE E: Drift, merge, extensions

**T16: Drift report and safe regeneration**
- Reverse run against existing specs produces added/removed/changed report.
- Regeneration preserves `manual` regions.
- Done when: a fixture with edited manual regions survives regeneration
  untouched.

**T17: Layered resolution engine**
- Implement `core -> java-profile -> extension -> project` resolution,
  manifest validation, `resolve --explain`.
- Done when: fixtures cover override, additive schema overlay, rejected
  weakening of an invariant, missing `reason`.

**T18: `spring-boot` extension**
- Stereotype detection, layered boundary rules, repository/controller
  templates, one prompt augmentation, one post-hook.
- Done when: monolith pipeline works with it enabled and its own tests pass.

**T19: Prove flexibility**
- Support PetClinic microservices using extensions and config **only**.
- Done when: zero changes in `core/`. If a change was needed, write an ADR
  describing the contract gap.

### PHASE F: Harden

**T20: Second-project test**
- Run the pipeline on one non-PetClinic Spring Boot project. List failures as
  backlog; fix only contract-level ones.

**T21: CI**
- Run all gates on every PR. Fail on unapproved drift or golden change.

**T22: Docs**
- README quickstart, "write an extension" guide, spec reference generated
  from schemas, known gaps list.

## 5. Suggested schedule (2 weeks)

| Days | Tasks |
|---|---|
| 1-2 | T01-T05 |
| 3-5 | T06-T09 |
| 6-8 | T10-T12 |
| 9-10 | T13-T15 |
| 11-12 | T16-T18 |
| 13 | T19 |
| 14 | T20-T22 (trim T20/T22 if behind; never trim gates) |

If a task slips, cut scope inside the task, not the tests or gates.

## 6. Prompt to start each session

Copy, fill in `<TASK_ID>`, and paste:

```
Read CONSTITUTION.md, CLAUDE.md, EXTENSION_CONTRACT.md and AGENT_HANDOFF.md.
Your task is <TASK_ID> from AGENT_HANDOFF.md section 4.
Follow the session protocol in section 2. Restate acceptance criteria, list
the files you will touch, write tests first, implement, run the gates, and
report real output. Do not start any other task. Do not invent structure;
record intent_gap instead. Update docs/PROGRESS.md when done.
```

## 7. Prompt for review sessions

```
Review the latest diff against CONSTITUTION.md only. Check for: invented
structure, ID stability, generated/manual boundary violations, core purity
(no Java/Spring in core, no core -> profile imports), weakened or skipped
gates, unjustified golden updates. Report violations with file and line.
Do not modify any files.
```

## 8. Stop conditions

Stop and ask the human when:
- A task needs a change to the constitution, ID rules, or extension contract.
- A gate must be weakened to pass.
- Acceptance criteria conflict with each other.
- The example repos have changed in a way that invalidates golden fixtures.
