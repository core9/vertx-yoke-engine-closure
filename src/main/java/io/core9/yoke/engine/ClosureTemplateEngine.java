package io.core9.yoke.engine;

import java.io.File;
import java.util.Map;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;

import com.google.template.soy.SoyFileSet;
import com.google.template.soy.tofu.SoyTofu;
import com.jetdrone.vertx.yoke.Engine;
import com.jetdrone.vertx.yoke.util.YokeAsyncResult;

/**
 * Closure Templates engine for Yoke 
 * @author <a href="mailto:mark.wienk@core9.io">Mark Wienk</a>
 *
 */
public class ClosureTemplateEngine extends Engine<String> {

	private static SoyTofu tofu;

	@Override
	public void render(final String file, final Map<String, Object> context, final Handler<AsyncResult<String>> handler) {
		if (tofu == null) {
			compileTemplates(vertx, new Handler<Boolean>() {
				@Override
				public void handle(Boolean event) {
					// Compile template
					handler.handle(new YokeAsyncResult<>(tofu.newRenderer(file.replace(".soy", "")).setData(context).render()));
				}
			});
		} else {
			// Compile template
			handler.handle(new YokeAsyncResult<>(tofu.newRenderer(file.replace(".soy", "")).setData(context).render()));
		}
	}
	
	@Override
	public void exists(String filename, final Handler<Boolean> next) {
        next.handle(true);
	}
	
	/**
	 * (Re)compile the templates
	 * @param vertx
	 * @param next
	 */
	public static void compileTemplates(final Vertx vertx, final Handler<Boolean> next) {
		vertx.fileSystem().exists("templates", new Handler<AsyncResult<Boolean>> () {
			@Override
			public void handle(AsyncResult<Boolean> event) {
				vertx.fileSystem().readDir("templates", new Handler<AsyncResult<String[]>>() {
					@Override
					public void handle(AsyncResult<String[]> event) {
						// Add all files in templates folder to templating engine
						SoyFileSet.Builder builder = new SoyFileSet.Builder();
						for(String file : event.result()) {
							builder.add(new File(file));
						}
						tofu = builder.build().compileToTofu();
						next.handle(true);
					}
				});
			}
		});
	}
}
