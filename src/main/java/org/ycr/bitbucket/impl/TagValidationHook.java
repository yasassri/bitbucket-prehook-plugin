package org.ycr.bitbucket.impl;

import com.atlassian.bitbucket.event.tag.TagCreationHookRequest;
import com.atlassian.bitbucket.hook.repository.*;
import com.atlassian.bitbucket.repository.*;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ycr.bitbucket.exceptions.TagValidationException;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

public class TagValidationHook implements PreRepositoryHook<TagCreationHookRequest> {

    private static final Logger log = LoggerFactory.getLogger(TagValidationHook.class);

    // Move this to configurations
    private static final String RELEASE_TAG_PATTERN = "^\\d+\\.\\d+\\.\\d+$";
    private static String VERSIONING_PATTERN = "^\\d+\\.\\d+\\.\\d+$";
    private static final String ENV_TAGS_PREFIXES = "dev,cqa";

    private final RefService refService;

    public TagValidationHook(
            @ComponentImport RefService refService) {
        this.refService = refService;
    }

    @Nonnull
    @Override
    public RepositoryHookResult preUpdate(@Nonnull PreRepositoryHookContext preRepositoryHookContext, @Nonnull TagCreationHookRequest tagCreationHookRequest) {
        // Reading configs from the plugin configs
        String branchName = preRepositoryHookContext.getSettings().getString("ref-id", "refs/heads/main");
        String tagPatternStr = preRepositoryHookContext.getSettings().getString("release-tag-regex", RELEASE_TAG_PATTERN);
        String envPrefixes = preRepositoryHookContext.getSettings().getString("env-prefixes", ENV_TAGS_PREFIXES);

        Pattern releaseTagPattern = Pattern.compile(tagPatternStr);
        Pattern varsioningPattern = Pattern.compile(VERSIONING_PATTERN);

        Tag tag = tagCreationHookRequest.getTag();
        String tagName = tag.getDisplayId();

        // CHeck if this is release tag
        if (releaseTagPattern.matcher(tagName).matches()) {
            Repository repository = tagCreationHookRequest.getRepository();
            String commitHash = tag.getLatestCommit();
            String lastCommitFromReleaseBranch = getLastCommitFromBranch(repository, branchName);

            if (!commitHash.equals(lastCommitFromReleaseBranch)) {
                System.out.println("This tag commit is not the latest commit in release branch");
                return RepositoryHookResult.rejected("Tag creation rejected", "The tag commit is not the latest commit in release branch, make sure you are creating the tag from the release branch");
            }
        } else {
            for (String prefix : envPrefixes.split(",")) {
                // If it's an env tag, we need to make sure the versioning is correct.
                String fullPrefix = prefix.trim().concat("-");
                if (tagName.startsWith(fullPrefix) && !varsioningPattern.matcher(tagName.substring(fullPrefix.length())).matches()) {
                    return RepositoryHookResult.rejected("Tag creation rejected", "The tag should adhere to the format: env-x.y.z, eg: dev-1.12.4");
                }
            }
        }
        return RepositoryHookResult.accepted();
    }

    private String getLastCommitFromBranch(Repository repository, String branchName) {

        ResolveRefRequest resolveRefRequest = new ResolveRefRequest.Builder(repository).refId(branchName).type(StandardRefType.BRANCH).build();
        Ref branchRef = refService.resolveRef(resolveRefRequest);
        if (branchRef == null) {
            throw new TagValidationException("Release branch not found: " + branchName);
        }

        return branchRef.getLatestCommit();
    }
}