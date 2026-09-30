# Extension Contract

Version: 0.1.0 · Governed by `CONSTITUTION.md` §6

An extension customises the framework without modifying core. If a need cannot
be met through the extension points below, that is a signal to add a new
extension point to the contract (via ADR), not to patch core.

## 1. Resolution order

    core -> java-profile -> extension(s) -> project

- Multiple extensions apply in the order listed in the project config.
- A later layer overrides an earlier one according to the merge rules (§4).
- `framework resolve --explain` prints the effective config and which layer
  supplied each value.

## 2. Extension layout

```
extensions/<name>/
  extension.yaml          # manifest (required)
  schemas/                # schema additions / overlays
  rules/                  # detection, grouping, boundary rules
  templates/              # generator templates
  prompts/                # prompt overrides for skills
  hooks/                  # pre/post step scripts
  tests/                  # extension's own golden tests
```

## 3. Manifest (`extension.yaml`)

```yaml
name: spring-boot                 # unique, kebab-case
version: 0.1.0                    # semver
framework: ">=0.1.0 <0.2.0"       # compatible core range
extends: [java-profile]           # layers/extensions this builds on
description: Spring Boot conventions for C3/C4

schemas:
  overlays:                       # add fields to existing schemas
    - target: component           # component | container | code | context
      file: schemas/component.spring.yaml

rules:
  detection:                      # deterministic detection of elements
    - id: spring-stereotypes
      target: component
      file: rules/spring-stereotypes.yaml
  grouping:                       # how classes group into components
    - id: package-and-stereotype
      file: rules/grouping.yaml
  boundaries:                     # source for generated ArchUnit rules
    - id: layered
      file: rules/boundaries.yaml

templates:
  - target: code.class            # what it generates
    match: {stereotype: repository}
    file: templates/repository.java.tpl
    mode: replace                 # replace | augment

prompts:
  - skill: c3-to-c2
    file: prompts/c3-to-c2.md
    mode: augment                 # replace | augment

hooks:
  - skill: c3-to-c4
    phase: post                   # pre | post
    run: hooks/format-and-headers.sh

overrides:                        # explicit overrides of lower layers
  - path: rules.boundaries.layered
    reason: Spring layering differs from generic Java profile
```

## 4. Extension points and merge rules

| Point | What it can do | Merge rule |
|---|---|---|
| Schema overlay | Add optional fields, add enum values, add stricter constraints | Additive only. Cannot remove fields, loosen required fields, or change types. |
| Detection rules | Add ways to recognise elements from code/config | Union. Duplicates by rule `id`: later layer replaces earlier. |
| Grouping rules | Change how C4 code elements form C3 components | Replace by rule `id`; ordered by `priority`. |
| Boundary rules | Define allowed dependencies between components | Replace by rule `id`; a rule set is versioned. |
| Templates | Change generated code/spec output | `replace` swaps the template; `augment` injects into declared slots. |
| Prompts | Change LLM instructions for a skill | `replace` or `augment`; output schema is unchanged and still validated. |
| Hooks | Run pre/post steps around a skill | Ordered by layer; a failing hook fails the run. |
| ID / trace rules | Not extensible | Fixed by the constitution. |

Invariants extensions **cannot** break:

- Element IDs, `parent`, `realizes`, `source`, `origin` fields.
- The generated/manual region semantics.
- Quality gates (an extension may add gates, never remove or weaken one).
- The skill output schemas (only additive overlays).

## 5. Plugin interfaces (for language profiles)

A profile or extension implementing a language provides these; Java is the
first implementation.

```
Extractor   detect(project) -> bool
            extract(project, level) -> Spec[]            # deterministic
Generator   generate(spec, templates, ctx) -> Files      # template-driven
Validator   validate(spec | files) -> Findings[]
Hook        run(phase, skill, ctx) -> Result
```

Contracts:

- `extract` is deterministic and idempotent: same input, same output,
  stable ordering.
- `generate` writes only to declared output paths and marks generated regions.
- `validate` returns findings with severity (`error` | `warning`) and the
  element ID concerned.

## 6. Project-level config (`c4.config.yaml`)

```yaml
framework: 0.1.0
profile: java-profile
extensions:
  - spring-boot
  - hexagonal            # order matters: later overrides earlier
project:
  name: petclinic
  base_package: org.springframework.samples.petclinic
overrides:
  - path: rules.grouping.package-and-stereotype.priority
    value: 10
    reason: Project packages are feature-first
```

## 7. Validation of extensions

On load, the framework checks that an extension:

1. Has a valid manifest and a compatible `framework` range.
2. Declares only additive schema overlays.
3. Lists every override with a `reason`.
4. Does not shadow an invariant in §4.
5. Passes its own `tests/` golden fixtures.

A failing check aborts the run with the extension name and the rule violated.

## 8. Versioning and compatibility

- Extensions and core follow semver. Core minor versions may add extension
  points; they may not remove or change existing ones without a major bump.
- Deprecations last at least one minor version and are surfaced as warnings.
- Schema overlays are validated against the target schema version they declare.

## 9. Acceptance test for the contract

The contract is considered proven when the PetClinic **microservices** variant
is supported by adding or configuring extensions only, with **zero changes to
`core/`**. Any core change needed is recorded as a contract gap and resolved by
ADR.
