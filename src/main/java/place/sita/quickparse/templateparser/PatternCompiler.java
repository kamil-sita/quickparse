package place.sita.quickparse.templateparser;

import place.sita.architecture.PrivateApi;
import place.sita.quickparse.Template;
import place.sita.quickparse.TemplateElement;
import place.sita.quickparse.exc.TemplateException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@PrivateApi
public class PatternCompiler {

    private static final char separator = '$';
    private static final char typeTagOpening = '(';
    private static final char typeTagClosing = ')';
    private static final char tagOpening = '{';
    private static final char tagClosing = '}';
    private static final char argTagOpening = '[';
    private static final char argTagClosing = ']';

    private final String template;

    private PatternCompiler(String template) {
        this.template = template;
    }

    public static Template build(String template) {
        return new PatternCompiler(template).preCompileInternal();
    }

    private int i = 0;
    private int segmentStartedAt = 0;
    private boolean isParsingBaseText = true;
    private boolean isInside = false;
    private boolean isComplexTag = false;

    List<TemplateElement> templateElements = new ArrayList<>();
    private TagType parsedTagType = null;

    private ArgParseContext argParseContext;

    private Template preCompileInternal() throws TemplateException {
        int length = template.length();

        for (; i < length; i++) {
            char c = template.charAt(i);
            if (isParsingBaseText) {
                parseBaseText(c);
            } else {
                parseTag(c);
            }
        }

        if (isParsingBaseText) {
            templateElements.add(new BaseText(template.substring(segmentStartedAt)));
        } else {
            if (isInside) {
                throw new TemplateException("Invalid template: unexpected end while analysing capture group");
            }
            templateElements.add(new CaptureGroupTemplateElement(argParseContext));
        }

        return new Template(templateElements);
    }

    private void parseBaseText(char c) {
        if (c == separator) {
            if (nextCharacterAlsoSeparator(i)) {
                templateElements.add(new BaseText(template.substring(segmentStartedAt, i)));
                i++;
                segmentStartedAt = i;
            } else {
                templateElements.add(new BaseText(template.substring(segmentStartedAt, i)));
                isParsingBaseText = false;
                argParseContext = new ArgParseContext();
            }
        }
    }

    private void parseTag(char c) {
        if (isInside) {
            if (isOpeningTag(c)) {
                throw new TemplateException("Cannot open a tag inside a tag");
            }
            if (isClosingTag(c)) {
                if (c != parsedTagType.closing) {
                    throw new TemplateException("Not the expected tag char ending. Got " + c + ", expected " + parsedTagType.closing);
                }
                isInside = false;
                String text = template.substring(segmentStartedAt, i);
                parsedTagType.stringAssignAction.accept(argParseContext, text);
            }
        } else {
            if (c == separator) {
                if (isComplexTag) {
                    templateElements.add(new CaptureGroupTemplateElement(argParseContext));
                    cleanupVarState(i + 1);
                } else {
                    if (nextCharacterAlsoSeparator(i)) {
                        templateElements.add(new CaptureGroupTemplateElement(argParseContext));
                        cleanupVarState(i);
                        i--;
                    } else {
                        throw new TemplateException("Cannot insert two consecutive capture groups");
                    }
                }
            } else if (isOpeningTag(c)) {
                isInside = true;
                isComplexTag = true;
                parsedTagType = getTag(c);
                if (parsedTagType.isIllegallyRedefined.apply(argParseContext)) {
                    throw new TemplateException("Type " + parsedTagType.name + " illegally redefined");
                }
                segmentStartedAt = i + 1;
            } else {
                if (isComplexTag) {
                    throw new TemplateException("Complex capture group must end with \"" + separator + "\"");
                }
                templateElements.add(new CaptureGroupTemplateElement(argParseContext));
                cleanupVarState(i);
            }
        }
    }

    private void cleanupVarState(int i) {
        argParseContext = null;
        isParsingBaseText = true;
        isComplexTag = false;
        segmentStartedAt = i;
    }

    public static CompiledTemplateImpl compile(Template template) {
        List<TemplateElement> templateElements = template.getElements();
        templateElements = cleanup(templateElements);

        for (int tid = 0; tid < templateElements.size(); tid++) {
            if (tid + 1 == templateElements.size()) {
                break;
            }
            TemplateElement element = templateElements.get(tid);
            TemplateElement nextElement = templateElements.get(tid + 1);
            if (element instanceof CaptureGroupTemplateElement && nextElement instanceof CaptureGroupTemplateElement) {
                throw new TemplateException("Cannot insert two consecutive capture groups");
            }
        }

        boolean anyCaptureGroupExists = false;
        boolean anyTextGroupExists = false;
        for (TemplateElement templateElement : templateElements) {
            if (templateElement instanceof CaptureGroupTemplateElement) {
                anyCaptureGroupExists = true;
            }
            if (templateElement instanceof BaseText) {
                anyTextGroupExists = true;
            }
            if (anyTextGroupExists && anyCaptureGroupExists) {
                break;
            }
        }
        if (!anyCaptureGroupExists) {
            throw new TemplateException("Invalid template: no capture groups!");
        }
        if (!anyTextGroupExists) {
            throw new TemplateException("Invalid template: no text groups!");
        }


        StringBuilder regexPatternBuilder = new StringBuilder();
        for (TemplateElement templateElement : templateElements) {
            regexPatternBuilder.append(templateElement.asRegex());
        }
        String regexPattern = regexPatternBuilder.toString();

        List<CaptureGroup> captureGroup = templateElements.stream()
                .filter(te -> te instanceof CaptureGroupTemplateElement)
                .map(templateElement -> new CaptureGroup(
                        ((CaptureGroupTemplateElement) templateElement).getName(),
                        ((CaptureGroupTemplateElement) templateElement).getType(),
                        ((CaptureGroupTemplateElement) templateElement).getArgs())
                ).collect(Collectors.toList());


        return new CompiledTemplateImpl(captureGroup, Pattern.compile(regexPattern));
    }

    private boolean nextCharacterAlsoSeparator(int i) {
        return notLast(i) && template.charAt(i + 1) == separator;
    }

    private boolean notLast(int i) {
        return i + 1 != template.length();
    }

    private static boolean isClosingTag(char c) {
        for (TagType tagType : TagType.values()) {
            if (c == tagType.closing) {
                return true;
            }
        }

        return false;
    }

    private static boolean isOpeningTag(char c) {
        for (TagType tagType : TagType.values()) {
            if (c == tagType.opening) {
                return true;
            }
        }

        return false;
    }

    public TagType getTag(char c) {
        for (TagType tagType : TagType.values()) {
            if (tagType.opening == c) {
                return tagType;
            }
        }
        throw new RuntimeException("No corresponding tag");
    }

    /**
     * Recursively cleans up generated template elements - cleanup includes removing empty text groups,
     * and merging adjacent text groups together.
     */
    private static List<TemplateElement> cleanup(List<TemplateElement> templateElements) {
        for (int i = 0; i < templateElements.size(); i++) {
            TemplateElement current = templateElements.get(i);

            if (current instanceof BaseText) {
                if (((BaseText) current).getText().isEmpty()) {
                    templateElements.remove(i);
                    return cleanup(templateElements);
                }
            }

            if (i + 1 == templateElements.size()) {
                break;
            }

            TemplateElement next = templateElements.get(i + 1);
            if (current instanceof BaseText && next instanceof BaseText) {
                BaseText baseText = new BaseText(((BaseText) current).getText() + ((BaseText) next).getText());
                templateElements.set(i, baseText);
                templateElements.remove(i + 1);
                return cleanup(templateElements);
            }
        }
        return templateElements;
    }

    private enum TagType {
        TYPE_TAG('(', ')', "type tag", ArgParseContext::setType, argParseContext -> argParseContext.getType() != null),
        NAME_TAG('{', '}', "name tag", ArgParseContext::setName, argParseContext -> argParseContext.getName() != null),
        ARG_TAG('[', ']', "arg tag", (argParseContext, s) -> {
            if (argParseContext.getArgs() == null) {
                argParseContext.setArgs(new ArrayList<>());
            }
            argParseContext.getArgs().add(s);
        }, argParseContext -> false);

        TagType(char opening, char closing, String name, BiConsumer<ArgParseContext, String> stringAssignAction, Function<ArgParseContext, Boolean> isIllegallyRedefined) {
            this.opening = opening;
            this.closing = closing;
            this.name = name;
            this.stringAssignAction = stringAssignAction;
            this.isIllegallyRedefined = isIllegallyRedefined;
        }

        private final char opening;
        private final char closing;
        private final String name;
        private final BiConsumer<ArgParseContext, String> stringAssignAction;
        private final Function<ArgParseContext, Boolean> isIllegallyRedefined;
    }

}
