fatals = [
    'astroid-error',
    'config-parse-error',
    'fatal',
    'method-check-failed',
    'parse-error',
]

errors = [
    'abstract-class-instantiated',
    'access-member-before-definition',
    'assigning-non-slot',
    'assignment-from-no-return',
    'assignment-from-none',
    'await-outside-async',
    'bad-configuration-section',
    'bad-except-order',
    'bad-exception-cause',
    'bad-format-character',
    'bad-plugin-value',
    'bad-reversed-sequence',
    'bad-str-strip-call',
    'bad-string-format-type',
    'bad-super-call',
    'bidirectional-unicode',
    'broken-collections-callable',
    'broken-noreturn',
    'catching-non-exception',
    'class-variable-slots-conflict',
    'continue-in-finally',
    'dict-iter-missing-items',
    'duplicate-argument-name',
    'duplicate-bases',
    'format-needs-mapping',
    'function-redefined',
    'import-error',
    'inconsistent-mro',
    'inherit-non-class',
    'init-is-generator',
    'invalid-all-format',
    'invalid-all-object',
    'invalid-bool-returned',
    'invalid-bytes-returned',
    'invalid-character-backspace',
    'invalid-character-carriage-return',
    'invalid-character-esc',
    'invalid-character-nul',
    'invalid-character-sub',
    'invalid-character-zero-width-space',
    'invalid-class-object',
    'invalid-enum-extension',
    'invalid-envvar-value',
    'invalid-field-call',
    'invalid-format-returned',
    'invalid-getnewargs-ex-returned',
    'invalid-getnewargs-returned',
    'invalid-hash-returned',
    'invalid-index-returned',
    'invalid-length-hint-returned',
    'invalid-length-returned',
    'invalid-metaclass',
    'invalid-repr-returned',
    'invalid-sequence-index',
    'invalid-slice-index',
    'invalid-slice-step',
    'invalid-slots',
    'invalid-slots-object',
    'invalid-star-assignment-target',
    'invalid-str-returned',
    'invalid-unary-operand-type',
    'invalid-unicode-codec',
    'logging-format-truncated',
    'logging-too-few-args',
    'logging-too-many-args',
    'logging-unsupported-format',
    'method-hidden',
    'misplaced-bare-raise',
    'misplaced-format-function',
    'missing-format-string-key',
    'missing-kwoa',
    'mixed-format-string',
    'modified-iterating-dict',
    'modified-iterating-set',
    'no-member',
    'no-method-argument',
    'no-name-in-module',
    'no-self-argument',
    'no-value-for-parameter',
    'non-iterator-returned',
    'nonexistent-operator',
    'nonlocal-and-global',
    'nonlocal-without-binding',
    'not-a-mapping',
    'not-an-iterable',
    'not-async-context-manager',
    'not-callable',
    'not-context-manager',
    'not-in-loop',
    'notimplemented-raised',
    'positional-only-arguments-expected',
    'potential-index-error',
    'raising-bad-type',
    'raising-non-exception',
    'redundant-keyword-arg',
    'relative-beyond-top-level',
    'repeated-keyword',
    'return-arg-in-generator',
    'return-in-init',
    'return-outside-function',
    'singledispatch-method',
    'singledispatchmethod-function',
    'star-needs-assignment-target',
    'syntax-error',
    'too-few-format-args',
    'too-many-format-args',
    'too-many-function-args',
    'too-many-star-expressions',
    'truncated-format-string',
    'undefined-all-variable',
    'undefined-variable',
    'unexpected-keyword-arg',
    'unexpected-special-method-signature',
    'unhashable-member',
    'unpacking-non-sequence',
    'unrecognized-inline-option',
    'unrecognized-option',
    'unsubscriptable-object',
    'unsupported-assignment-operation',
    'unsupported-binary-operation',
    'unsupported-delete-operation',
    'unsupported-membership-test',
    'used-before-assignment',
    'used-prior-global-declaration',
    'yield-inside-async-function',
    'yield-outside-function',
]

warnings = [
    'abstract-method',
    'anomalous-backslash-in-string',
    'anomalous-unicode-escape-in-string',
    'arguments-differ',
    'arguments-out-of-order',
    'arguments-renamed',
    'assert-on-string-literal',
    'assert-on-tuple',
    'attribute-defined-outside-init',
    'bad-builtin',
    'bad-chained-comparison',
    'bad-dunder-name',
    'bad-format-string',
    'bad-format-string-key',
    # 'bad-indentation',  # this is subjective
    'bad-open-mode',
    'bad-staticmethod-argument',
    'bad-thread-instantiation',
    'bare-except',
    'binary-op-exception',
    'boolean-datetime',
    'broad-exception-caught',
    'broad-exception-raised',
    'cell-var-from-loop',
    'comparison-with-callable',
    'confusing-with-statement',
    # 'consider-ternary-expression',
    'dangerous-default-value',
    'deprecated-argument',
    'deprecated-class',
    'deprecated-decorator',
    'deprecated-method',
    'deprecated-module',
    'deprecated-typing-alias',
    'differing-param-doc',
    'differing-type-doc',
    'duplicate-except',
    'duplicate-key',
    'duplicate-string-formatting-argument',
    'duplicate-value',
    # 'eq-without-hash',
    'eval-used',
    'exec-used',
    'expression-not-assigned',
    'f-string-without-interpolation',
    'fixme',
    'forgotten-debug-statement',
    'format-combined-specification',
    'format-string-without-interpolation',
    'global-at-module-level',
    'global-statement',
    'global-variable-not-assigned',
    'global-variable-undefined',
    'implicit-flag-alias',
    'implicit-str-concat',
    'import-self',
    # 'inconsistent-quotes',
    'invalid-envvar-default',
    'invalid-format-index',
    'invalid-overridden-method',
    'isinstance-second-argument-not-valid-type',
    'keyword-arg-before-vararg',
    'kwarg-superseded-by-positional-arg',
    'logging-format-interpolation',
    'logging-fstring-interpolation',
    'logging-not-lazy',
    'lost-exception',
    'method-cache-max-size-none',
    'misplaced-future',
    'missing-any-param-doc',
    'missing-format-argument-key',
    'missing-format-attribute',
    # 'missing-param-doc',
    'missing-parentheses-for-call-in-test',
    # 'missing-raises-doc',
    # 'missing-return-doc',
    # 'missing-return-type-doc',
    'missing-timeout',
    # 'missing-type-doc',
    # 'missing-yield-doc',
    # 'missing-yield-type-doc',
    'modified-iterating-list',
    'multiple-constructor-doc',
    'named-expr-without-context',
    'nan-comparison',
    'nested-min-max',
    'non-ascii-file-name',
    'non-parent-init-called',
    'non-str-assignment-to-dunder-name',
    'overlapping-except',
    'overridden-final-method',
    'pointless-exception-statement',
    'pointless-statement',
    'pointless-string-statement',
    'possibly-unused-variable',
    'preferred-module',
    'protected-access',
    'raise-missing-from',
    'raising-format-tuple',
    'redeclared-assigned-name',
    'redefined-builtin',
    'redefined-loop-name',
    'redefined-outer-name',
    'redefined-slots-in-subclass',
    'redundant-returns-doc',
    'redundant-u-string-prefix',
    'redundant-unittest-assert',
    'redundant-yields-doc',
    'reimported',
    'return-in-finally',
    'self-assigning-variable',
    'self-cls-assignment',
    'shadowed-import',
    'shallow-copy-environ',
    'signature-differs',
    'subclassed-final-class',
    'subprocess-popen-preexec-fn',
    'subprocess-run-check',
    # 'super-init-not-called',
    'super-without-brackets',
    'too-many-try-statements',
    'try-except-raise',
    'unbalanced-dict-unpacking',
    'unbalanced-tuple-unpacking',
    'undefined-loop-variable',
    'unknown-option-value',
    'unnecessary-ellipsis',
    'unnecessary-lambda',
    # 'unnecessary-pass',
    'unnecessary-semicolon',
    'unreachable',
    'unspecified-encoding',
    'unused-argument',
    'unused-format-string-argument',
    'unused-format-string-key',
    'unused-import',
    'unused-private-member',
    'unused-variable',
    'unused-wildcard-import',
    'useless-else-on-loop',
    'useless-param-doc',
    'useless-parent-delegation',
    'useless-type-doc',
    'useless-with-lock',
    'using-constant-test',
    'using-f-string-in-unsupported-version',
    'using-final-decorator-in-unsupported-version',
    # 'while-used',  # too broad
    'wildcard-import',
    'wrong-exception-operation',
]

conventions = [
    'bad-classmethod-argument',
    # 'bad-docstring-quotes',
    'bad-file-encoding',
    'bad-mcs-classmethod-argument',
    'bad-mcs-method-argument',
    'consider-iterating-dictionary',
    'consider-using-any-or-all',
    'consider-using-dict-items',
    'consider-using-enumerate',
    'consider-using-f-string',
    'dict-init-mutate',
    'disallowed-name',
    # 'docstring-first-line-empty',
    # 'empty-docstring',
    'import-outside-toplevel',
    'import-private-name',
    # 'invalid-characters-in-docstring',
    # 'invalid-name',
    'line-too-long',
    'misplaced-comparison-constant',
    # 'missing-class-docstring',
    # 'missing-final-newline',
    # 'missing-function-docstring',
    # 'missing-module-docstring',
    'mixed-line-endings',
    'multiple-imports',
    'multiple-statements',
    'non-ascii-module-import',
    'non-ascii-name',
    'single-string-used-for-slots',
    'singleton-comparison',
    # 'superfluous-parens',
    # 'too-many-lines',
    # 'trailing-newlines',
    # 'trailing-whitespace',
    'typevar-double-variance',
    'typevar-name-incorrect-variance',
    'typevar-name-mismatch',
    'unexpected-line-ending-format',
    'ungrouped-imports',
    'unidiomatic-typecheck',
    'unnecessary-direct-lambda-call',
    'unnecessary-dunder-call',
    'unnecessary-lambda-assignment',
    'unnecessary-negation',
    'use-implicit-booleaness-not-comparison',
    'use-implicit-booleaness-not-comparison-to-string',
    'use-implicit-booleaness-not-comparison-to-zero',
    'use-implicit-booleaness-not-len',
    'use-maxsplit-arg',
    'use-sequence-for-iteration',
    'useless-import-alias',
    # 'wrong-import-order',
    'wrong-import-position',
    # 'wrong-spelling-in-comment',
    # 'wrong-spelling-in-docstring',
]

refactors = [
    'chained-comparison',
    'comparison-of-constants',
    'comparison-with-itself',
    'condition-evals-to-constant',
    # 'confusing-consecutive-elif',
    'consider-alternative-union-syntax',
    'consider-merging-isinstance',
    'consider-refactoring-into-while-condition',
    'consider-swap-variables',
    'consider-using-alias',
    # 'consider-using-assignment-expr',
    # 'consider-using-augmented-assign',
    'consider-using-dict-comprehension',
    'consider-using-from-import',
    'consider-using-generator',
    'consider-using-get',
    'consider-using-in',
    'consider-using-join',
    'consider-using-max-builtin',
    'consider-using-min-builtin',
    'consider-using-namedtuple-or-dataclass',
    'consider-using-set-comprehension',
    'consider-using-sys-exit',
    'consider-using-ternary',
    'consider-using-tuple',
    'consider-using-with',
    'cyclic-import',
    'duplicate-code',
    'else-if-used',
    # 'empty-comment',
    'inconsistent-return-statements',
    'literal-comparison',
    # 'magic-value-comparison',
    'no-classmethod-decorator',
    # 'no-else-break',
    # 'no-else-continue',
    # 'no-else-raise',
    # 'no-else-return',
    # 'no-self-use',
    'no-staticmethod-decorator',
    'prefer-typing-namedtuple',
    'property-with-parameters',
    'redefined-argument-from-local',
    'redefined-variable-type',
    'redundant-typehint-argument',
    'simplifiable-condition',
    'simplifiable-if-expression',
    'simplifiable-if-statement',
    'simplify-boolean-expression',
    'stop-iteration-return',
    'super-with-arguments',
    'too-complex',
    # 'too-few-public-methods',
    # 'too-many-ancestors',
    # 'too-many-arguments',
    'too-many-boolean-expressions',
    'too-many-branches',
    'too-many-instance-attributes',
    # 'too-many-locals',
    'too-many-nested-blocks',
    # 'too-many-public-methods',
    # 'too-many-return-statements',
    'too-many-statements',
    'trailing-comma-tuple',
    'unnecessary-comprehension',
    'unnecessary-dict-index-lookup',
    'unnecessary-list-index-lookup',
    'use-a-generator',
    'use-dict-literal',
    'use-list-literal',
    'use-set-for-membership',
    'useless-object-inheritance',
    'useless-option-value',
    'useless-return',
]

all_messages = fatals + errors + warnings + conventions + refactors
