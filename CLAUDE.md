# CLAUDE.md: Working Rules for AI Agents

Read `CONSTITUTION.md` first. It overrides anything below. Then read any ADR in
`docs/adr/` relevant to your task.

## Repo layout

```
core/
  specs/            JSON Schemas per level (versioned)
  skills/<a>-to-<b>/  SKILL.md, prompts/, validator, golden/
  trace/            ID registry rules, link validator
  plugin-api/       Extractor / Generator / Validator interfaces
java-profile/       Generic Java conventions, extractors, templates
extensions/<name>/  extension.yaml + overrides (see EXTENSION_CONTRACT.md)
examples/           PetClinic (monolith, microservices) as forks/submodules
evals/              Golden fixtures + scoring scripts
docs/adr/           Architecture decision records
```

## How to work

1. **One narrow task per session.** If the task touches more than one skill,
   schema, or layer, split it.
2. **Tests first.** Write or update the failing test / golden fixture, then the
   implementation. State the acceptance criteria before coding.
3. **Plan, then edit.** Briefly list files you will change and why. Do not
   touch files outside that list without saying so.
4. **Run the gates** (see below) before declaring done. Report actual output,
   not assumptions.
5. **Small commits.** One logical change per commit, message in the form
   `<area>: <change>` (e.g. `c3-to-c4: add repository stub template`).

## Hard rules

- Never invent structure. If a class, dependency, container, or component is
  not from an extractor result or explicit human input, do not add it.
  Record an `intent_gap` instead.
- Never change an ID once assigned. Never reuse an ID.
- Never edit `manual` regions of a spec. Only `generated` regions.
- Never change a schema without: (a) bumping its version, (b) updating
  contract tests, (c) adding an ADR if the change is breaking.
- Never put Java, Spring, or org-specific logic in `core/`. It belongs in
  `java-profile/` or an extension.
- Never make `core/` import from a profile or extension.
- Never silence a failing gate. If a gate is wrong, fix the gate in a
  separate, explained change.
- Never update golden files just to make a test pass. State why the new
  output is correct.
- Do not paste large generated outputs into prompts; reference file paths.

## LLM usage inside skills

- Prompts live in `prompts/` as versioned files, not inline strings.
- LLM output must be structured (JSON/YAML) and validated before use.
- LLM may set only fields the skill declares as `inferred`, each with
  `origin: inferred` and a `confidence`.
- Prefer passing extractor output as context; do not ask the LLM to
  re-derive it.

## Quality gates (run in this order)

```
make validate-schemas     # specs conform to JSON Schema
make validate-links       # IDs unique, parent/realizes resolve, no orphans
make compile-generated    # generated Java compiles
make test-boundaries      # generated ArchUnit rules pass
make test-golden          # outputs match golden fixtures
make test-roundtrip       # extract -> generate -> extract has no drift
make eval                 # LLM skill scoring thresholds
```

If a `make` target does not exist yet, create it as part of the task, with a
minimal implementation, rather than skipping the gate.

## Definition of done

- [ ] Acceptance tests written first and now passing
- [ ] All applicable gates pass, with output shown
- [ ] No constitution or ADR violations
- [ ] Docs / SKILL.md updated for behaviour changes
- [ ] Known gaps listed explicitly (not hidden)

## Review pass (second agent or second session)

Review the diff against `CONSTITUTION.md` only. Check: invented structure,
ID stability, generated/manual boundary, core purity, gate integrity. Report
violations with file and line; do not fix in the same pass.

## When unsure

Ask a specific question or record an `intent_gap` / ADR proposal. Do not
guess and proceed.
