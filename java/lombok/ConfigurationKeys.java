/*     */ package lombok;
/*     */ 
/*     */ import java.util.List;
/*     */ import lombok.core.configuration.CallSuperType;
/*     */ import lombok.core.configuration.CheckerFrameworkVersion;
/*     */ import lombok.core.configuration.ConfigurationKey;
/*     */ import lombok.core.configuration.FlagUsageType;
/*     */ import lombok.core.configuration.IdentifierName;
/*     */ import lombok.core.configuration.LogDeclaration;
/*     */ import lombok.core.configuration.NullCheckExceptionType;
/*     */ import lombok.core.configuration.TypeName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConfigurationKeys
/*     */ {
/*  50 */   public static final ConfigurationKey<Boolean> LOMBOK_DISABLE = new ConfigurationKey<Boolean>("dangerousconfig.lombok.disable", "Disables lombok transformers. It does not flag any lombok mentions (so, @Cleanup silently does nothing), and does not disable patched operations in eclipse either. Don't use this unless you know what you're doing. (default: false).", true)
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*  64 */   public static final ConfigurationKey<Boolean> ADD_GENERATED_ANNOTATIONS = new ConfigurationKey<Boolean>("lombok.addGeneratedAnnotation", "Generate @javax.annotation.Generated on all generated code (default: false). Deprecated, use 'lombok.addJavaxGeneratedAnnotation' instead.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   public static final ConfigurationKey<Boolean> ADD_JAVAX_GENERATED_ANNOTATIONS = new ConfigurationKey<Boolean>("lombok.addJavaxGeneratedAnnotation", "Generate @javax.annotation.Generated on all generated code (default: follow lombok.addGeneratedAnnotation).")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*  80 */   public static final ConfigurationKey<Boolean> ADD_LOMBOK_GENERATED_ANNOTATIONS = new ConfigurationKey<Boolean>("lombok.addLombokGeneratedAnnotation", "Generate @lombok.Generated on all generated code (default: false).")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   public static final ConfigurationKey<Boolean> ADD_FINDBUGS_SUPPRESSWARNINGS_ANNOTATIONS = new ConfigurationKey<Boolean>("lombok.extern.findbugs.addSuppressFBWarnings", "Generate @edu.umd.cs.findbugs.annotations.SuppressFBWarnings on all generated code (default: false).")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public static final ConfigurationKey<FlagUsageType> ANY_CONSTRUCTOR_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.anyConstructor.flagUsage", "Emit a warning or error if any of the XxxArgsConstructor annotations are used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 115 */   public static final ConfigurationKey<Boolean> ANY_CONSTRUCTOR_SUPPRESS_CONSTRUCTOR_PROPERTIES = new ConfigurationKey<Boolean>("lombok.anyConstructor.suppressConstructorProperties", "Suppress the generation of @ConstructorProperties for generated constructors (default: false).")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   public static final ConfigurationKey<Boolean> ANY_CONSTRUCTOR_ADD_CONSTRUCTOR_PROPERTIES = new ConfigurationKey<Boolean>("lombok.anyConstructor.addConstructorProperties", "Generate @ConstructorProperties for generated constructors (default: false).")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 130 */   public static final ConfigurationKey<FlagUsageType> ALL_ARGS_CONSTRUCTOR_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.allArgsConstructor.flagUsage", "Emit a warning or error if @AllArgsConstructor is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 137 */   public static final ConfigurationKey<FlagUsageType> NO_ARGS_CONSTRUCTOR_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.noArgsConstructor.flagUsage", "Emit a warning or error if @NoArgsConstructor is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 144 */   public static final ConfigurationKey<Boolean> NO_ARGS_CONSTRUCTOR_EXTRA_PRIVATE = new ConfigurationKey<Boolean>("lombok.noArgsConstructor.extraPrivate", "Generate a private no-args constructor for @Data and @Value (default: true).")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 151 */   public static final ConfigurationKey<FlagUsageType> REQUIRED_ARGS_CONSTRUCTOR_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.requiredArgsConstructor.flagUsage", "Emit a warning or error if @RequiredArgsConstructor is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 162 */   public static final ConfigurationKey<FlagUsageType> DATA_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.data.flagUsage", "Emit a warning or error if @Data is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 171 */   public static final ConfigurationKey<FlagUsageType> VALUE_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.value.flagUsage", "Emit a warning or error if @Value is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 180 */   public static final ConfigurationKey<FlagUsageType> GETTER_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.getter.flagUsage", "Emit a warning or error if @Getter is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 187 */   public static final ConfigurationKey<FlagUsageType> GETTER_LAZY_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.getter.lazy.flagUsage", "Emit a warning or error if @Getter(lazy=true) is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 195 */   public static final ConfigurationKey<Boolean> GETTER_CONSEQUENT_BOOLEAN = new ConfigurationKey<Boolean>("lombok.getter.noIsPrefix", "If true, generate and use getFieldName() for boolean getters instead of isFieldName().")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 204 */   public static final ConfigurationKey<FlagUsageType> SETTER_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.setter.flagUsage", "Emit a warning or error if @Setter is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 213 */   public static final ConfigurationKey<Boolean> EQUALS_AND_HASH_CODE_DO_NOT_USE_GETTERS = new ConfigurationKey<Boolean>("lombok.equalsAndHashCode.doNotUseGetters", "Don't call the getters but use the fields directly in the generated equals and hashCode method (default = false).")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 220 */   public static final ConfigurationKey<CallSuperType> EQUALS_AND_HASH_CODE_CALL_SUPER = new ConfigurationKey<CallSuperType>("lombok.equalsAndHashCode.callSuper", "When generating equals and hashCode for classes that extend something (other than Object), either automatically take into account superclass implementation (call), or don't (skip), or warn and don't (warn). (default = warn).")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 227 */   public static final ConfigurationKey<FlagUsageType> EQUALS_AND_HASH_CODE_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.equalsAndHashCode.flagUsage", "Emit a warning or error if @EqualsAndHashCode is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 236 */   public static final ConfigurationKey<Boolean> TO_STRING_DO_NOT_USE_GETTERS = new ConfigurationKey<Boolean>("lombok.toString.doNotUseGetters", "Don't call the getters but use the fields directly in the generated toString method (default = false).")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 243 */   public static final ConfigurationKey<CallSuperType> TO_STRING_CALL_SUPER = new ConfigurationKey<CallSuperType>("lombok.toString.callSuper", "When generating toString for classes that extend something (other than Object), either automatically take into account superclass implementation (call), or don't (skip), or warn and don't (warn). (default = warn).")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 250 */   public static final ConfigurationKey<FlagUsageType> TO_STRING_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.toString.flagUsage", "Emit a warning or error if @ToString is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 257 */   public static final ConfigurationKey<Boolean> TO_STRING_INCLUDE_FIELD_NAMES = new ConfigurationKey<Boolean>("lombok.toString.includeFieldNames", "Include the field names in the generated toString method (default = true).")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 266 */   public static final ConfigurationKey<String> BUILDER_CLASS_NAME = new ConfigurationKey<String>("lombok.builder.className", "Default name of the generated builder class. A * is replaced with the name of the relevant type (default = *Builder).")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 273 */   public static final ConfigurationKey<FlagUsageType> BUILDER_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.builder.flagUsage", "Emit a warning or error if @Builder is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 283 */   public static final ConfigurationKey<Boolean> SINGULAR_USE_GUAVA = new ConfigurationKey<Boolean>("lombok.singular.useGuava", "Generate backing immutable implementations for @Singular on java.util.* types by using guava's ImmutableList, etc. Normally java.util's mutable types are used and wrapped to make them immutable.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 290 */   public static final ConfigurationKey<Boolean> SINGULAR_AUTO = new ConfigurationKey<Boolean>("lombok.singular.auto", "If true (default): Automatically singularize the assumed-to-be-plural name of your variable/parameter when using {@code @Singular}.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 301 */   public static final ConfigurationKey<FlagUsageType> CLEANUP_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.cleanup.flagUsage", "Emit a warning or error if @Cleanup is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 310 */   public static final ConfigurationKey<FlagUsageType> DELEGATE_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.delegate.flagUsage", "Emit a warning or error if @Delegate is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 320 */   public static final ConfigurationKey<NullCheckExceptionType> NON_NULL_EXCEPTION_TYPE = new ConfigurationKey<NullCheckExceptionType>("lombok.nonNull.exceptionType", "The type of the exception to throw if a passed-in argument is null (Default: NullPointerException).")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 327 */   public static final ConfigurationKey<FlagUsageType> NON_NULL_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.nonNull.flagUsage", "Emit a warning or error if @NonNull is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 336 */   public static final ConfigurationKey<FlagUsageType> SNEAKY_THROWS_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.sneakyThrows.flagUsage", "Emit a warning or error if @SneakyThrows is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 345 */   public static final ConfigurationKey<FlagUsageType> SYNCHRONIZED_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.synchronized.flagUsage", "Emit a warning or error if @Synchronized is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 354 */   public static final ConfigurationKey<FlagUsageType> VAL_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.val.flagUsage", "Emit a warning or error if 'val' is used.") {  }
/* 355 */   ; public static final ConfigurationKey<FlagUsageType> VAR_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.var.flagUsage", "Emit a warning or error if 'var' is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 364 */   public static final ConfigurationKey<FlagUsageType> WITH_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.with.flagUsage", "Emit a warning or error if @With is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 374 */   public static final ConfigurationKey<FlagUsageType> LOG_ANY_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.log.flagUsage", "Emit a warning or error if any of the log annotations is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 381 */   public static final ConfigurationKey<FlagUsageType> LOG_COMMONS_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.log.apacheCommons.flagUsage", "Emit a warning or error if @CommonsLog is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 388 */   public static final ConfigurationKey<FlagUsageType> LOG_JUL_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.log.javaUtilLogging.flagUsage", "Emit a warning or error if @Log is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 395 */   public static final ConfigurationKey<FlagUsageType> LOG_LOG4J_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.log.log4j.flagUsage", "Emit a warning or error if @Log4j is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 402 */   public static final ConfigurationKey<FlagUsageType> LOG_LOG4J2_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.log.log4j2.flagUsage", "Emit a warning or error if @Log4j2 is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 409 */   public static final ConfigurationKey<FlagUsageType> LOG_SLF4J_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.log.slf4j.flagUsage", "Emit a warning or error if @Slf4j is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 416 */   public static final ConfigurationKey<FlagUsageType> LOG_XSLF4J_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.log.xslf4j.flagUsage", "Emit a warning or error if @XSlf4j is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 423 */   public static final ConfigurationKey<FlagUsageType> LOG_JBOSSLOG_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.log.jbosslog.flagUsage", "Emit a warning or error if @JBossLog is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 430 */   public static final ConfigurationKey<FlagUsageType> LOG_FLOGGER_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.log.flogger.flagUsage", "Emit a warning or error if @Flogger is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 437 */   public static final ConfigurationKey<IdentifierName> LOG_ANY_FIELD_NAME = new ConfigurationKey<IdentifierName>("lombok.log.fieldName", "Use this name for the generated logger fields (default: 'log').")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 446 */   public static final ConfigurationKey<Boolean> LOG_ANY_FIELD_IS_STATIC = new ConfigurationKey<Boolean>("lombok.log.fieldIsStatic", "Make the generated logger fields static (default: true).")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 455 */   public static final ConfigurationKey<FlagUsageType> LOG_CUSTOM_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.log.custom.flagUsage", "Emit a warning or error if @CustomLog is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 480 */   public static final ConfigurationKey<LogDeclaration> LOG_CUSTOM_DECLARATION = new ConfigurationKey<LogDeclaration>("lombok.log.custom.declaration", "Define the generated custom logger field.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 490 */   public static final ConfigurationKey<FlagUsageType> EXPERIMENTAL_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.experimental.flagUsage", "Emit a warning or error if an experimental feature is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 499 */   public static final ConfigurationKey<FlagUsageType> ACCESSORS_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.accessors.flagUsage", "Emit a warning or error if @Accessors is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 506 */   public static final ConfigurationKey<List<String>> ACCESSORS_PREFIX = new ConfigurationKey<List<String>>("lombok.accessors.prefix", "Strip this field prefix, like 'f' or 'm_', from the names of generated getters and setters.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 513 */   public static final ConfigurationKey<Boolean> ACCESSORS_CHAIN = new ConfigurationKey<Boolean>("lombok.accessors.chain", "Generate setters that return 'this' instead of 'void' (default: false).")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 520 */   public static final ConfigurationKey<Boolean> ACCESSORS_FLUENT = new ConfigurationKey<Boolean>("lombok.accessors.fluent", "Generate getters and setters using only the field name (no get/set prefix) (default: false).")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 529 */   public static final ConfigurationKey<FlagUsageType> EXTENSION_METHOD_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.extensionMethod.flagUsage", "Emit a warning or error if @ExtensionMethod is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 538 */   public static final ConfigurationKey<Boolean> FIELD_DEFAULTS_PRIVATE_EVERYWHERE = new ConfigurationKey<Boolean>("lombok.fieldDefaults.defaultPrivate", "If true, fields without any access modifier, in any file (lombok annotated or not) are marked as private. Use @PackagePrivate or an explicit modifier to override this.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 545 */   public static final ConfigurationKey<Boolean> FIELD_DEFAULTS_FINAL_EVERYWHERE = new ConfigurationKey<Boolean>("lombok.fieldDefaults.defaultFinal", "If true, fields, in any file (lombok annotated or not) are marked as final. Use @NonFinal to override this.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 552 */   public static final ConfigurationKey<FlagUsageType> FIELD_DEFAULTS_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.fieldDefaults.flagUsage", "Emit a warning or error if @FieldDefaults is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 561 */   public static final ConfigurationKey<FlagUsageType> HELPER_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.helper.flagUsage", "Emit a warning or error if @Helper is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 572 */   public static final ConfigurationKey<FlagUsageType> ON_X_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.onX.flagUsage", "Emit a warning or error if onX is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 581 */   public static final ConfigurationKey<FlagUsageType> UTILITY_CLASS_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.utilityClass.flagUsage", "Emit a warning or error if @UtilityClass is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 589 */   public static final ConfigurationKey<FlagUsageType> FIELD_NAME_CONSTANTS_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.fieldNameConstants.flagUsage", "Emit a warning or error if @FieldNameConstants is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 596 */   public static final ConfigurationKey<IdentifierName> FIELD_NAME_CONSTANTS_INNER_TYPE_NAME = new ConfigurationKey<IdentifierName>("lombok.fieldNameConstants.innerTypeName", "The default name of the inner type generated by @FieldNameConstants. (default: 'Fields').")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 603 */   public static final ConfigurationKey<Boolean> FIELD_NAME_CONSTANTS_UPPERCASE = new ConfigurationKey<Boolean>("lombok.fieldNameConstants.uppercase", "The default name of the constants inside the inner type generated by @FieldNameConstants follow the variable name precisely. If this config key is true, lombok will uppercase them as best it can. (default: false).")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 612 */   public static final ConfigurationKey<FlagUsageType> SUPERBUILDER_FLAG_USAGE = new ConfigurationKey<FlagUsageType>("lombok.superBuilder.flagUsage", "Emit a warning or error if @SuperBuilder is used.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 623 */   public static final ConfigurationKey<Boolean> STOP_BUBBLING = new ConfigurationKey<Boolean>("config.stopBubbling", "Tell the configuration system it should stop looking for other configuration files (default: false).")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 630 */   public static final ConfigurationKey<List<TypeName>> COPYABLE_ANNOTATIONS = new ConfigurationKey<List<TypeName>>("lombok.copyableAnnotations", "Copy these annotations to getters, setters, with methods, builder-setters, etc.")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 639 */   public static final ConfigurationKey<CheckerFrameworkVersion> CHECKER_FRAMEWORK = new ConfigurationKey<CheckerFrameworkVersion>("checkerframework", "If set with the version of checkerframework.org (in major.minor, or just 'true' for the latest supported version), create relevant checkerframework.org annotations for code lombok generates (default: false).") {
/*     */     
/*     */     };
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\lombok\ConfigurationKeys.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */